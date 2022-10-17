/* Name: Saul Weintraub
 * Course: CSC352 Spring Session 2022
 * Date: 4/19/2022
 *
 * This file uses the 3d library defined in 3d.h and implemented in 3d.c in order to create a
 * custom scene composed of 10 objects. This scene is stored in the file output.stl.
 *
 */
#include <stdio.h>
#include <stdint.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include "3d.h"

int main(){
	Scene3D* s = Scene3D_create();
	int levels = 1;
	for (int i = 1; i >= 0; i--) {
		for (int j = 0; j < 3; j++) {
			Coordinate3D origin = (Coordinate3D){j*100, i*100, 0};
			Object3D* object = Object3D_create_fractal(origin, 50, levels);
			Scene3D_append(s, object);
			printf("count for level %d: %ld\n", levels, object->count);
			levels += 1;
		}
	}
	Scene3D_write_stl_binary(s, "fractal.stl");
	Scene3D_destroy(s);
}
