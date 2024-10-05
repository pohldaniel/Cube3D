#include <SDL2/SDL.h>
#include <SDL2/SDL_image.h>
#include "engine.h"

engine eng;

void setup_textures();
void setup_decals();
void setup_actors();
void loop_handler();
bool should_continue_logic_loops();
void setup_bindings();

engine *engine_init(
        unsigned int w,
        unsigned int h)
{
    eng.fps = 80;
    eng.current_frame = 0;
    eng.score = 0;

    eng.start_time = 0;
    eng.should_start_logic_loop = true;
    eng.whole_frames_to_do = 0;

    SDL_Init(SDL_INIT_VIDEO);
    TTF_Init();

    SDL_CreateWindowAndRenderer(w, h, 0, &eng.window, &eng.renderer);
    eng.w = w;
    eng.h = h;

    if(eng.window == NULL) {
        fprintf(
                stderr,
                "Window could not be created: %s\n", SDL_GetError());
        return NULL;
    }

    setup_textures();
    setup_decals();

    eng.grid_width = w / TILE_DIMENSION;
    eng.grid_height = (h / TILE_DIMENSION) - STATUS_BAR_HEIGHT;
    eng.occupied_gridpoints = malloc(eng.grid_width * eng.grid_height * sizeof(*eng.occupied_gridpoints));
    memset(eng.occupied_gridpoints, 0, eng.grid_width * eng.grid_height * sizeof(*eng.occupied_gridpoints));

    SDL_StartTextInput();
    setup_bindings();

    eng.render_list = NULL;
    eng.logic_list = NULL;

    setup_actors();

    return &eng;
}

void engine_destroy()
{
    SDL_Quit();
}

void engine_start()
{
    eng.start_time = SDL_GetTicks();

    emscripten_set_main_loop(loop_handler, -1, 0);
}

void engine_reset()
{
    snake_actor_init(&eng.snake_actor);
    memset(eng.occupied_gridpoints, 0, eng.grid_width * eng.grid_height * sizeof(*eng.occupied_gridpoints));
    apple_actor_init(&eng.apple_actor);
    eng.score = 0;
    background_actor_update_scoreboard(&eng.background_actor);
}

void setup_textures()
{
    char * filenames[] = {
        "assets/sand.png",
        "assets/sprites.png",
        "assets/status_bar.png"
    };
    int i;

    for (i = 0; i < sizeof(filenames) / sizeof(filenames[0]); i++) {
        char * fname = filenames[i];
        SDL_Surface *img = IMG_Load(fname);

        if(!img){
            fprintf(stdout, "Error! Could not load %s\n", fname);
            exit(1);
        }
        
        eng.textures[i] = SDL_CreateTextureFromSurface(eng.renderer, img);

        SDL_FreeSurface(img);
    }
}

void setup_decals()
{
    int i;

    int imgw, imgh;
    SDL_QueryTexture(eng.textures[SPRITES_TEXTURE],
            NULL, NULL, &imgw, &imgh);

    for (i = 0; i < NUM_SPRITES_DECALS; i++) {
        double w = 1/8. * imgw;
        double h = 1/2. * imgh;
        double x = (double)(i % 8) * w;
        double y = (double)(i / 8) * h;

        decal_init(
                &eng.sprites_decals[i],
                eng.textures[SPRITES_TEXTURE],
                x,
                y,
                w,
                h);
    }

    SDL_QueryTexture(eng.textures[SAND_TEXTURE],
            NULL, NULL, &imgw, &imgh);

    decal_init(
        &(eng.sand_decal),
        eng.textures[SAND_TEXTURE],
        0,
        0,
        imgw,
        imgh);
}

void setup_actors()
{
    snake_actor_init(&eng.snake_actor);
    eng.render_list = actor_list_add(eng.render_list, (actor *)(&eng.snake_actor));
    eng.logic_list = actor_list_add(eng.logic_list, (actor *)(&eng.snake_actor));

    apple_actor_init(&eng.apple_actor);
    eng.render_list = actor_list_add(eng.render_list, (actor *)(&eng.apple_actor));

    background_actor_init(&eng.background_actor);
    eng.render_list = actor_list_add(eng.render_list, (actor *)(&eng.background_actor));
}

bool should_continue_logic_loops()
{
    if (eng.should_start_logic_loop) {
        unsigned int logic_loop_start_time = SDL_GetTicks();
        double elapsed_frames = (double)(logic_loop_start_time \
                - eng.start_time) / 1000.0f * eng.fps;

        eng.whole_frames_to_do = (unsigned int)elapsed_frames - eng.current_frame;
    }

    if (!eng.whole_frames_to_do) {
        eng.should_start_logic_loop = true;
        return false;
    }

    eng.whole_frames_to_do -= 1;
    eng.current_frame += 1;
    eng.should_start_logic_loop = false;
    return true;
}

void loop_handler()
{
    process_input();

//    if (is_state_active(GS_QUIT)) {
//        return;
//    }

    SDL_RenderClear(eng.renderer);

    actor_list *al;
    for (al = eng.render_list; al != NULL; al = al->next) {
        al->a->render_handler(al->a);
    }

    while (should_continue_logic_loops()) {
        for (al = eng.logic_list; al != NULL; al = al->next) {
            al->a->logic_handler(al->a);
        }
    }

    SDL_RenderPresent(eng.renderer);
}

void setup_bindings()
{
    input_processor_init();
    key_state_binding binding;

    binding.k = SDLK_UP;
    binding.s = GS_N;
    binding.t = BINDING_ONE_TIME;
    add_binding(&binding);

    binding.k = SDLK_RIGHT;
    binding.s = GS_E;
    add_binding(&binding);

    binding.k = SDLK_DOWN;
    binding.s = GS_S;
    add_binding(&binding);

    binding.k = SDLK_LEFT;
    binding.s = GS_W;
    add_binding(&binding);
}

void engine_get_grid_coord(const int *pixel_coord, int *grid_coord)
{
    grid_coord[0] = pixel_coord[0] / TILE_DIMENSION;
    grid_coord[1] = pixel_coord[1] / TILE_DIMENSION - STATUS_BAR_HEIGHT;
}

void engine_get_pixel_coord(const int *grid_coord, int *pixel_coord)
{
    pixel_coord[0] = grid_coord[0] * TILE_DIMENSION;
    pixel_coord[1] = (grid_coord[1] + STATUS_BAR_HEIGHT) * TILE_DIMENSION;
}

void engine_apply_boundary_conditions(int *grid_coords)
{
    grid_coords[0] = ((grid_coords[0] % eng.grid_width) + eng.grid_width) % eng.grid_width;
    grid_coords[1] = ((grid_coords[1] % eng.grid_height) + eng.grid_height) % eng.grid_height;
}
