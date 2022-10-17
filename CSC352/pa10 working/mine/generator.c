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
	Scene3D* custom = Scene3D_create();

	Coordinate3D origin = (Coordinate3D){100, 100, 100};
	Object3D* object = Object3D_create_cuboid(origin, 20, 40, 20);
	Scene3D_append(custom, object);

	origin = (Coordinate3D){100, 120, 100};
	object = Object3D_create_pyramid(origin, 20, 30, "up");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){110, 90, 100};
	object = Object3D_create_pyramid(origin, 20, 30, "right");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){90, 90, 100};
	object = Object3D_create_pyramid(origin, 20, 30, "left");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){100, 90, 110};
	object = Object3D_create_pyramid(origin, 20, 30, "forward");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){100, 90, 90};
	object = Object3D_create_pyramid(origin, 20, 30, "backward");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){140, 50, 100};
	object = Object3D_create_pyramid(origin, 10, 40, "up");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){60, 50, 100};
	object = Object3D_create_pyramid(origin, 10, 40, "up");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){100, 50, 140};
	object = Object3D_create_pyramid(origin, 10, 40, "up");
	Scene3D_append(custom, object);

	origin = (Coordinate3D){100, 50, 60};
	object = Object3D_create_pyramid(origin, 10, 40, "up");
	Scene3D_append(custom, object);

	Scene3D_write_stl_binary(custom, "output.stl");
	Scene3D_destroy(custom);
	return 0;
}
