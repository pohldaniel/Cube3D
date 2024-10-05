CC = emcc
all: main.c engine.c engine.h sprite.c sprite.h decal.c decal.h \
 	actor.c actor.h apple_actor.c apple_actor.h snake_actor.c snake_actor.h \
	background_actor.c background_actor.h input_processor.c input_processor.h
	$(CC) main.c engine.c sprite.c decal.c actor.c apple_actor.c snake_actor.c background_actor.c input_processor.c -O2 -s TOTAL_MEMORY=67108864 -s USE_SDL=2 -s USE_SDL_IMAGE=2 -s USE_SDL_TTF=2 -s "SDL2_IMAGE_FORMATS=['png']" -s "EXPORTED_FUNCTIONS=['_mainf']" --preload-file assets -o snake.js
