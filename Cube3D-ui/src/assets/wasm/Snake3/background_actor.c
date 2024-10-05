#include "engine.h"

void background_render_handler(actor *a)
{
    background_actor *bg = (background_actor *)a;

    sprite_render(&(bg->background_sprite));
    SDL_Rect dest = {.x = 0, .y = 0, .w = eng.grid_width * TILE_DIMENSION, .h = STATUS_BAR_HEIGHT * TILE_DIMENSION};
    SDL_RenderCopy (eng.renderer, bg->status_bar, NULL, &dest);

    SDL_QueryTexture(bg->status_text,
            NULL, NULL, &dest.w, &dest.h);
    dest.x = 80;
    dest.y = 20;
    SDL_RenderCopy (eng.renderer, bg->status_text, NULL, &dest);
}

background_actor *background_actor_init(background_actor *bg)
{
    actor_init(&bg->a, background_render_handler, NULL);
    sprite_init(
            &bg->background_sprite,
            eng.grid_width * TILE_DIMENSION,
            eng.grid_height * TILE_DIMENSION,
            &eng.sand_decal);
    bg->background_sprite.r[0] = 0;
    bg->background_sprite.r[1] = STATUS_BAR_HEIGHT * TILE_DIMENSION;

    bg->status_bar = eng.textures[STATUS_BAR_TEXTURE]; // SDL_CreateTextureFromSurface(eng.renderer, status_surface);

    bg->font = TTF_OpenFont(STATUS_TEXT_FONT, STATUS_TEXT_SIZE);
    background_actor_update_scoreboard(bg);

    return bg;
}

background_actor *background_actor_update_scoreboard(background_actor *bg)
{
	SDL_Color fg={93,93,93,255};
    char msg[201];
    sprintf(msg, "score: %02u", eng.score);
    SDL_Surface *status_text_surface = TTF_RenderText_Blended(bg->font, msg, fg);
    bg->status_text = SDL_CreateTextureFromSurface(eng.renderer, status_text_surface);
    SDL_FreeSurface(status_text_surface);

    return bg;
}
