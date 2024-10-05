#include "engine.h"
#include <stdlib.h>

void apple_render_handler(actor *a)
{
    apple_actor *ap = (apple_actor *)a;

    sprite_render(&ap->sprite);
}

apple_actor *apple_actor_init(apple_actor *ap)
{
    actor_init(&ap->a, apple_render_handler, NULL);
    sprite_init(
            &ap->sprite,
            TILE_DIMENSION,
            TILE_DIMENSION,
            &eng.sprites_decals[APPLE]);
    int grid_coords[] = {2, 2};
    engine_get_pixel_coord(grid_coords, ap->sprite.r);
    return ap;
}

void select_random_gridpoint(int *i)
{
    i[0] = rand() % eng.grid_width;
    i[1] = rand() % eng.grid_height;
}

void apple_replace(apple_actor *ap)
{
    int i[2];
    bool placed_apple = false;

    while (!placed_apple)
    {
        select_random_gridpoint(i);

        if (!eng.occupied_gridpoints[i[0] + eng.grid_width * i[1]])
        {
            engine_get_pixel_coord(i, ap->sprite.r);
            placed_apple = true;
        }
    }
}

