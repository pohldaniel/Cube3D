#ifndef BACKGROUND_ACTOR_H
#define BACKGROUND_ACTOR_H
#include "engine.h"

typedef struct background_actor background_actor;

struct background_actor {
    actor a;
    sprite background_sprite;

    TTF_Font *font;
    SDL_Texture *status_bar;
    SDL_Texture *status_text;
};

background_actor *background_actor_init(background_actor *bg);
background_actor *background_actor_update_scoreboard(background_actor *bg);
#endif

