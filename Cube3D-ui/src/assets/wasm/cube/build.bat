emcc -Os SOIL/src/image_DXT.c SOIL/src/image_helper.c SOIL/src/SOIL.c SOIL/src/stb_image_aug.c 3d-cube.c -o 3d-cube.js -s LEGACY_GL_EMULATION=1 -I SOIL/src -s EXPORTED_RUNTIME_METHODS="['ccall']" -s FORCE_FILESYSTEM=1 -s MODULARIZE=1 -s EXPORT_NAME='Cube3dModule'

cmd /k