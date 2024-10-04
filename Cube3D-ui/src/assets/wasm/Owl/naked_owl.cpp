#include <stdio.h>
#include <iostream>
#include <SDL2/SDL.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_ttf.h>
#include <emscripten.h>
#include <emscripten/html5.h>
#define GL_GLEXT_PROTOTYPES
#define EGL_EGLEXT_PROTOTYPES
#include <GL/gl.h>
EM_JS(int, canvas_get_width, (), {
  return canvas.width;
});

EM_JS(int, canvas_get_height, (), {
  return canvas.height;
});

static bool sizeChanged = false;
static int WIDTH = 0;
static int HEIGHT = 0;

typedef struct Vertex { float x, y, r, g, b; } Vertex;
static GLuint program, vertex_buffer, index_buffer;
static GLint uMVP_location, aPos_location, aTex_location, uT_location;
GLfloat mvp[4*4];
GLfloat t[4*4];
GLfloat s[4*4];
/**
 * Inverse square root of two, for normalising velocity
 */
#define REC_SQRT2 0.7071067811865475 

/**
 * Set of input states
 */
enum input_state
{
    NOTHING_PRESSED = 0,
    UP_PRESSED = 1,
    DOWN_PRESSED = 1<<1,
    LEFT_PRESSED = 1<<2,
    RIGHT_PRESSED = 1<<3
};

/**
 * Context structure that will be passed to the loop handler
 */
struct context
{
    SDL_Renderer *renderer;
	SDL_Window *window;
	SDL_GLContext context;
    /**
     * Rectangle that the owl texture will be rendered into
     */
    SDL_Rect dest;
    SDL_Texture *owl_tex;

    /**
     * Font that is used for rendering text, and
     * a texture the text is rendered into
     */
    TTF_Font *font;
    SDL_Texture *text_tex;

    enum input_state active_state;

    /**
     * x and y components of owl's velocity
     */
    int owl_vx;
    int owl_vy;
};

/**
 * Loads the owl texture into the context
 */
int get_owl_texture(struct context *ctx)
{
  SDL_Surface *image = IMG_Load("assets/owl.png");
  if (!image)
  {
     printf("IMG_Load: %s\n", IMG_GetError());
     return 0;
  }
  ctx->owl_tex = SDL_CreateTextureFromSurface(ctx->renderer, image);
  ctx->dest.w = image->w;
  ctx->dest.h = image->h;

  SDL_FreeSurface (image);

  return 1;
}

/**
 * Set the context's text texture to show the text 'text' 
 */
void set_font_text(struct context *ctx, const char *text)
{
	SDL_Color fg = {0,0,0,255};
    SDL_Surface *text_surface = TTF_RenderText_Blended(ctx->font, text, fg);
    ctx->text_tex = SDL_CreateTextureFromSurface(ctx->renderer, text_surface);
    SDL_FreeSurface(text_surface);
}

/**
 * Load the font we're going to use and set the text to
 * be "Hello owl!"
 */
int get_font_texture(struct context *ctx)
{
    ctx->font = TTF_OpenFont("assets/FreeSans.ttf", 30);
    set_font_text(ctx, "Hello owl!");
    return 1;
}

/**
 * Processes the input events and sets the velocity
 * of the owl accordingly
 */
void process_input(struct context *ctx)
{
    SDL_Event event;

    while (SDL_PollEvent(&event)) {
        switch (event.key.keysym.sym)
        {
            case SDLK_UP:
                if (event.key.type == SDL_KEYDOWN)
                    ctx->active_state =  static_cast<input_state>(ctx->active_state | UP_PRESSED);
                else if (event.key.type == SDL_KEYUP)
                    ctx->active_state = static_cast<input_state>(ctx->active_state ^ UP_PRESSED);
                break;
            case SDLK_DOWN:
                if (event.key.type == SDL_KEYDOWN)
                    ctx->active_state = static_cast<input_state>(ctx->active_state | DOWN_PRESSED);
                else if (event.key.type == SDL_KEYUP)
                    ctx->active_state = static_cast<input_state>(ctx->active_state ^ DOWN_PRESSED);
                break;
            case SDLK_LEFT:
                if (event.key.type == SDL_KEYDOWN)
                    ctx->active_state = static_cast<input_state>(ctx->active_state | LEFT_PRESSED);
                else if (event.key.type == SDL_KEYUP)
                    ctx->active_state = static_cast<input_state>(ctx->active_state ^ LEFT_PRESSED);
                break;
            case SDLK_RIGHT:
                if (event.key.type == SDL_KEYDOWN)
                    ctx->active_state = static_cast<input_state>(ctx->active_state | RIGHT_PRESSED);
                else if (event.key.type == SDL_KEYUP)
                    ctx->active_state = static_cast<input_state>(ctx->active_state ^ RIGHT_PRESSED);
                break;
            break;
            default:
                break;
        }
    }

    ctx->owl_vy = 0;
    ctx->owl_vx = 0;
    if (ctx->active_state & UP_PRESSED)
        ctx->owl_vy = -5;
    if (ctx->active_state & DOWN_PRESSED)
        ctx->owl_vy = 5;
    if (ctx->active_state & LEFT_PRESSED)
        ctx->owl_vx = -5;
    if (ctx->active_state & RIGHT_PRESSED)
        ctx->owl_vx = 5;

    if (ctx->owl_vx != 0 && ctx->owl_vy != 0)
    {
        ctx->owl_vx *= REC_SQRT2;
        ctx->owl_vy *= REC_SQRT2;
    }
}

void Orthographic(GLfloat* mtx, float left, float right, float bottom, float top, float znear, float zfar){
	mtx[0] = 2.0f / (right - left); mtx[1] = 0.0f; mtx[2] = 0.0f; mtx[3] = 0.0f;
	mtx[4] = 0.0f; mtx[5] = 2.0f / (top - bottom); mtx[6] = 0.0f; mtx[7] = 0.0f;
	mtx[8] = 0.0f; mtx[9] = 0.0f; mtx[10] = 1.0f; mtx[11] = 0.0f;
	mtx[12] = (right + left) / (left - right); mtx[13] = (top + bottom) / (bottom - top); mtx[14] = (zfar + znear) / (znear - zfar); mtx[15] = 1.0f;
}

void Scale(GLfloat* mtx, float a, float b, float c){
	mtx[0] = a; mtx[1] = 0.0f; mtx[2] = 0.0f; mtx[3] = 0.0f;
	mtx[4] = 0.0f; mtx[5] = b; mtx[6] = 0.0f; mtx[7] = 0.0f;
	mtx[8] = 0.0f; mtx[9] = 0.0f; mtx[10] = c; mtx[11] = 0.0f;
	mtx[12] = 0.0f; mtx[13] = 0.0f; mtx[14] = 0.0f; mtx[15] = 1.0f;
}

void Translate(GLfloat* mtx, float dx, float dy, float dz){
	mtx[0] = 1.0f; mtx[1] = 0.0f; mtx[2] = 0.0f; mtx[3] = 0.0f;
	mtx[4] = 0.0f; mtx[5] = 1.0f; mtx[6] = 0.0f; mtx[7] = 0.0f;
	mtx[8] = 0.0f; mtx[9] = 0.0f; mtx[10] = 1.0f; mtx[11] = 0.0f;
	mtx[12] = dx; mtx[13] = dy; mtx[14] = dz; mtx[15] = 1.0f;
}

void Multiply(GLfloat* mtxa, GLfloat* mtxb){
	float tmp0 = mtxa[0];float tmp1 = mtxa[4];float tmp2 = mtxa[8];float tmp3 = mtxa[12];
	mtxa[0] =  (tmp0 * mtxb[0])  + (tmp1 * mtxb[1])  + (tmp2 * mtxb[2])   + (tmp3 * mtxb[3]);
	mtxa[4] =  (tmp0 * mtxb[4])  + (tmp1 * mtxb[5])  + (tmp2 * mtxb[6])   + (tmp3 * mtxb[7]);
	mtxa[8] =  (tmp0 * mtxb[8])  + (tmp1 * mtxb[9])  + (tmp2 * mtxb[10])  + (tmp3 * mtxb[11]);
	mtxa[12] = (tmp0 * mtxb[12]) + (tmp1 * mtxb[13]) + (tmp2 * mtxb[14])  + (tmp3 * mtxb[15]);
	
	tmp0 = mtxa[1]; tmp1 = mtxa[5]; tmp2 = mtxa[9]; tmp3 = mtxa[13];
	mtxa[1] =  (tmp0 * mtxb[0])  + (tmp1 * mtxb[1])  + (tmp2 * mtxb[2])   + (tmp3 * mtxb[3]);
	mtxa[5] =  (tmp0 * mtxb[4])  + (tmp1 * mtxb[5])  + (tmp2 * mtxb[6])   + (tmp3 * mtxb[7]);
	mtxa[9] =  (tmp0 * mtxb[8])  + (tmp1 * mtxb[9])  + (tmp2 * mtxb[10])  + (tmp3 * mtxb[11]);
	mtxa[13] = (tmp0 * mtxb[12]) + (tmp1 * mtxb[13]) + (tmp2 * mtxb[14])  + (tmp3 * mtxb[15]);
	
	tmp0 = mtxa[2]; tmp1 = mtxa[6]; tmp2 = mtxa[10]; tmp3 = mtxa[14];
	mtxa[2] =  (tmp0* mtxb[0])   + (tmp1 * mtxb[1])  + (tmp2 * mtxb[2])  + (tmp3 * mtxb[3]);
	mtxa[6] =  (tmp0 * mtxb[4])  + (tmp1 * mtxb[5])  + (tmp2 * mtxb[6])  + (tmp3 * mtxb[7]);
	mtxa[10] = (tmp0 * mtxb[8])  + (tmp1 * mtxb[9])  + (tmp2 * mtxb[10]) + (tmp3 * mtxb[11]);
	mtxa[14] = (tmp0 * mtxb[12]) + (tmp1 * mtxb[13]) + (tmp2 * mtxb[14]) + (tmp3 * mtxb[15]);
	
	tmp0 = mtxa[3]; tmp1 = mtxa[7]; tmp2 = mtxa[11]; tmp3 = mtxa[15];
	mtxa[3] =  (tmp0 * mtxb[0])  + (tmp1 * mtxb[1])  + (tmp2 * mtxb[2])  + (tmp3 * mtxb[3]);
	mtxa[7] =  (tmp0 * mtxb[4])  + (tmp1 * mtxb[5])  + (tmp2 * mtxb[6])  + (tmp3 * mtxb[7]);
	mtxa[11] = (tmp0 * mtxb[8])  + (tmp1 * mtxb[9])  + (tmp2 * mtxb[10]) + (tmp3 * mtxb[11]);
	mtxa[15] = (tmp0 * mtxb[12]) + (tmp1 * mtxb[13]) + (tmp2 * mtxb[14]) + (tmp3 * mtxb[15]);
}

/**
 * Loop handler that gets called each animation frame,
 * process the input, update the position of the owl and 
 * then render the texture
 */
void loop_handler(void *arg)
{
    struct context *ctx = (context*)arg;
	
	if (sizeChanged){
		SDL_SetWindowSize(ctx->window, WIDTH, HEIGHT);		
		sizeChanged = false;
		glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	process_input(ctx);
	ctx->dest.x += ctx->owl_vx;
    ctx->dest.y += ctx->owl_vy;

	glClear(GL_COLOR_BUFFER_BIT);

	Vertex vertices[4] =
	{
		{ 0.0f,  0.0f, 0.0f, 0.0f, 1.0f},
		{ 0.0f,  1.0f, 0.0f, 0.0f, 0.0f},
		{ 1.0f,  1.0f, 0.0f, 1.0f, 0.0f},
		{ 1.0f,  0.0f, 0.0f, 1.0f, 1.0f},		
	};
	
	const GLushort index[] = {
		0, 2, 1,
		0, 2, 3
	};
	SDL_GL_BindTexture(ctx->owl_tex, NULL, NULL);
	
	glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
	glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

	Scale(s, 200.0f, 200.0f, 1.0f);
	Translate(t, ctx->dest.x, (static_cast<float>(400.0f)) * 0.5f - ctx->dest.y, 0.0f);
	
	Orthographic(mvp, 0.0f, 600.0f, 0.0f, 400.0f, -1.0f, 1.0f);
	glUseProgram(program);
	Multiply(t, s);
	Multiply(mvp, t);
	glUniformMatrix4fv(uMVP_location, 1, GL_FALSE, mvp);
	
	//Indices	
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, index_buffer);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(index), index, GL_STATIC_DRAW);
	glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
	
	SDL_Rect text_dest = {.x = 50 , .y = 175, .w = 0, .h = 0};
    SDL_QueryTexture(ctx->text_tex, NULL, NULL, &text_dest.w, &text_dest.h);
	
	Scale(s, text_dest.w, text_dest.h, 1.0f);
	Translate(t, 50.0f, 175.0f, 0.0f);
	Orthographic(mvp, 0.0f, 600.0f, 0.0f, 400.0f, -1.0f, 1.0f);
	Multiply(t, s);
	Multiply(mvp, t);
	glUniformMatrix4fv(uMVP_location, 1, GL_FALSE, mvp);
	SDL_GL_BindTexture(ctx->text_tex, NULL, NULL);
	glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
	
    SDL_RenderPresent(ctx->renderer);
}


extern "C" void setSize(int width, int height) {
  sizeChanged = true;
  //float fov = static_cast<float>(height)/static_cast<float>(width);
  WIDTH = width;
  HEIGHT = height;
}

extern "C" void mainf()
{
    struct context ctx;

    SDL_Init(SDL_INIT_VIDEO);
    TTF_Init();
	WIDTH = 600;
	HEIGHT = 400;
	ctx.window = SDL_CreateWindow( "Cube3D", 0, 0, 600, 400, SDL_WINDOW_OPENGL | SDL_WINDOW_RESIZABLE );
    ctx.context = SDL_GL_CreateContext(ctx.window);
	ctx.renderer =  SDL_CreateRenderer(ctx.window, -1, 0);

    //SDL_CreateWindowAndRenderer(600, 400, SDL_WINDOW_OPENGL | SDL_WINDOW_RESIZABLE, &ctx.window, &ctx.renderer);
    SDL_SetRenderDrawColor(ctx.renderer, 255, 255, 255, 255);
	SDL_SetWindowResizable(ctx.window, SDL_TRUE);
	
    get_owl_texture(&ctx);
    get_font_texture(&ctx);
    ctx.active_state = NOTHING_PRESSED;
    ctx.dest.x = 200;
    ctx.dest.y = 100;
    ctx.owl_vx = 0;
    ctx.owl_vy = 0;
	
	glViewport(0, 0, 600, 400);
	glEnable(GL_BLEND);
	glEnable(GL_TEXTURE_2D);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	
	const char* vertex_shader_text =
	"precision lowp float;"
	"uniform mat4 uMVP;"
	"uniform mat4 uT;"
	"attribute vec3 aPos;"
	"attribute vec2 aTex;"
	"varying vec2 vTex;"
	"void main()"
	"{"
		"vTex = aTex;"
		"gl_Position = uMVP * vec4(aPos, 1.0);"
	"}";

	const char* fragment_shader_text =
	"precision lowp float;"
	"varying vec2 vTex;"
	"uniform sampler2D u_texture;"
	"void main()"
	"{" 
		"float color = texture2D(u_texture, vTex).r;"
		"gl_FragColor =  texture2D(u_texture, vTex);"
	"}";

	GLuint vertex_shader = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(vertex_shader, 1, &vertex_shader_text, 0);
	glCompileShader(vertex_shader);

	GLuint fragment_shader = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(fragment_shader, 1, &fragment_shader_text, 0);
	glCompileShader(fragment_shader);

	program = glCreateProgram();
	glAttachShader(program, vertex_shader);
	glAttachShader(program, fragment_shader);
	glLinkProgram(program);

	uMVP_location = glGetUniformLocation(program, "uMVP");
	uT_location = glGetUniformLocation(program, "uT");
	aPos_location = glGetAttribLocation(program, "aPos");
	aTex_location = glGetAttribLocation(program, "aTex");

	glGenBuffers(1, &vertex_buffer);
	glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);

	glEnableVertexAttribArray(aPos_location);
	glVertexAttribPointer(aPos_location, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)0);
	glEnableVertexAttribArray(aTex_location);
	glVertexAttribPointer(aTex_location, 2, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)(sizeof(float) * 3));

	glGenBuffers(1, &index_buffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, index_buffer);

    emscripten_set_main_loop_arg(loop_handler, &ctx, -1, 1);
	
	SDL_GL_DeleteContext(ctx.context);
	SDL_DestroyRenderer(ctx.renderer);
	SDL_DestroyWindow(ctx.window);
	ctx.context = NULL;
	ctx.renderer = NULL;
	ctx.window = NULL;
	SDL_Quit();
}