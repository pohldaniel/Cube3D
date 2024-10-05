#ifndef ENGINE_H
#define ENGINE_H

#include <SDL2/SDL.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_ttf.h>
#include <emscripten.h>
#include <stdbool.h>

#include "input_processor.h"
#include "sprite.h"
#include "actor.h"

typedef struct engine engine;

typedef enum DIRECTION
{
    DIRECTION_N,
    DIRECTION_E,
    DIRECTION_S,
    DIRECTION_W
} DIRECTION;

enum
{
    SAND_TEXTURE,
    SPRITES_TEXTURE,
    STATUS_BAR_TEXTURE,
    NUM_TEXTURES
};

typedef enum SPRITES_DECALS
{
    TAIL_S,
    TAIL_W,
    HEAD_N,
    HEAD_E,
    BODY_SE,
    BODY_SW,
    BODY_NW,
    BLANK,
    TAIL_E,
    TAIL_N,
    HEAD_W,
    HEAD_S,
    BODY_N,
    BODY_E,
    BODY_NE,
    APPLE,
    NUM_SPRITES_DECALS
} SPRITES_DECALS;

enum {
    MAX_SNAKE_SEGMENTS = 50,
    TILE_DIMENSION = 40,
    STATUS_BAR_HEIGHT = 2, // given in tiles
    STATUS_TEXT_SIZE = 30
};

#define STATUS_TEXT_FONT "assets/edosz.ttf"

#define INITIAL_SNAKE_SECONDS_PER_UPDATE 0.2
#define MINIMUM_SNAKE_SECONDS_PER_UPDATE 0.005
#define SNAKE_SECONDS_PER_UPDATE_INCREMENT 0.01

#include "apple_actor.h"
#include "snake_actor.h"
#include "background_actor.h"

/*typedef struct engine_
{
    unsigned int fps;
    unsigned int current_frame;
    SDL_Window *window;
    SDL_Renderer *renderer;
    unsigned int w;
    unsigned int h;

    Uint32 start_time;
    bool should_start_logic_loop;
    unsigned int whole_frames_to_do;

    actor_list *render_list;
    actor_list *logic_list;

    SDL_Texture *textures[NUM_TEXTURES];
    decal sprites_decals[NUM_SPRITES_DECALS];
    decal sand_decal;

    apple_actor apple_actor;
    background_actor background_actor;
    // status_bar_actor status_bar_actor;
    snake_actor snake_actor;

    unsigned int score;
    unsigned int *occupied_gridpoints;
    int grid_width;
    int grid_height;
} engine;*/


struct engine 
{
    unsigned int fps;
    unsigned int current_frame;
    SDL_Window *window;
    SDL_Renderer *renderer;
    unsigned int w;
    unsigned int h;

    Uint32 start_time;
    bool should_start_logic_loop;
    unsigned int whole_frames_to_do;

    actor_list *render_list;
    actor_list *logic_list;

    SDL_Texture *textures[NUM_TEXTURES];
    decal sprites_decals[NUM_SPRITES_DECALS];
    decal sand_decal;

    apple_actor apple_actor;
    background_actor background_actor;
    // status_bar_actor status_bar_actor;
    snake_actor snake_actor;

    unsigned int score;
    unsigned int *occupied_gridpoints;
    int grid_width;
    int grid_height;
};

extern engine eng;

engine *engine_init(
        unsigned int w,
        unsigned int h);

void engine_destroy();

void engine_start();

void engine_reset();

void engine_get_grid_coord(const int *pixel_coord, int *grid_coord);
void engine_get_pixel_coord(const int *grid_coord, int *pixel_coord);
void engine_apply_boundary_conditions(int *grid_coords);

#endif
