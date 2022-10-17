/* Name: Saul Weintraub
 * Course: CSC352 Spring Session 2022
 * Date: 4/19/2022
 *
 * This file implements the functions defined in 3d.h and acts as a library that can create data
 * structures that represent scenes in a 3-dimensional space. This library also contains functions
 * that allow the user to convert these objects to a .stl file.
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

// BENS FUNCTIONS
/**
 * A Helper function to append a Triangle3DNode to an Object3D.
 *   Parameters:
 *     object - the object to append to
 *     node   - the node to append
 */
void Object3D_append_object_node(Object3D* object, Triangle3DNode* node) {
  object->count += 1;
  if (object->root == NULL) {
    object->root = node;
    return;
  }
  Triangle3DNode* temp = object->root;
  while (temp->next != NULL) {
     temp = temp->next;
  }
  temp->next = node;
}

/**
 * A Helper function to append a Triangle3D to an Object3D.
 *   Parameters:
 *     object   - the object to append to
 *     triangle - the triangle to append
 */
void Object3D_append_triangle(Object3D* object, Triangle3D triangle) {
  Triangle3DNode* node = calloc(1, sizeof(Triangle3DNode));
  node->triangle = triangle;
  Object3D_append_object_node(object, node);
}

/**
 * Compute the distance between two points.
 *   Parameters:
 *     a / b - Coordinates in 3D space
 *   Return:
 *     The computed distance represented as a double
 */
double Coordinate3D_distance(Coordinate3D a, Coordinate3D b) {
  return sqrt( pow(a.x - b.x, 2.0) + pow(a.y - b.y, 2.0) + pow(a.z - b.z, 2.0));
}

/**
 * Determine the two closest points, and the farthest point, from a given point.
 *   Parameters:
 *     starting - The coordinate to begin distance measuring from
 *     coords   - An array of 3 coordinates 
 *     closest1 / closest2 - The two closest point to starting
 *     Farthest - The farthest point from starting
 */
void Coordinate3D_get_closest_two(
    Coordinate3D starting, Coordinate3D* coords, 
    Coordinate3D* closest1, Coordinate3D* closest2,
    Coordinate3D* farthest) {
  double zero = Coordinate3D_distance(starting, coords[0]);
  double one  = Coordinate3D_distance(starting, coords[1]);
  double two  = Coordinate3D_distance(starting, coords[2]);
  if (zero <= two && one <= two) {
    *closest1 = coords[0];
    *closest2 = coords[1];
    *farthest = coords[2];
  } else if (one <= zero && two <= zero) {
    *closest1 = coords[1];
    *closest2 = coords[2];
    *farthest = coords[0];
  } else if (two <= one && zero <= one){
    *closest1 = coords[0];
    *closest2 = coords[2];
    *farthest = coords[1];
  } else {
    assert(false);
  }
}

#define DOUBLE_MARGIN 0.0001

/**
 * Sort a list of Coordinate3Ds.
 *   Parameters:
 *     array - The array of coordinates to sort
 *     size - the number of elements in the array of coordinates
 */
void Coordinate3D_sort(Coordinate3D* array, int size) {
  for (int i = 0; i < size; i++) {
    for (int j = 0; j < size-1; j++) {
      Coordinate3D ca = array[j];
      Coordinate3D cb = array[j+1];
      if ( (ca.x - cb.x) >= DOUBLE_MARGIN ) {
        array[j] = cb; array[j+1] = ca;
      } else if ( (cb.x - ca.x) >= DOUBLE_MARGIN ) {
        continue;
      } else if ( (ca.y - cb.y) >= DOUBLE_MARGIN ) {
        array[j] = cb; array[j+1] = ca;
      } else if ( (cb.y - ca.y) >= DOUBLE_MARGIN ) {
        continue;
      } else if ( (ca.z - cb.z) >= DOUBLE_MARGIN ) {
        array[j] = cb; array[j+1] = ca;
      }
    }
  }
}

void Object3D_append_quadrilateral(Object3D* o, 
    Coordinate3D a, Coordinate3D b, Coordinate3D c, Coordinate3D d) {

  Coordinate3D starting, closest1, closest2, farthest;
  Coordinate3D co[] = {a, b, c, d};
  Coordinate3D_sort(co, 4);
  a = co[0]; b = co[1]; c = co[2]; d = co[3];
  double max_distance = 0.0;
  double distances[6] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  int ci = 0;
  int maxi = 0;
  
  // Find the coordinate that is furthest from the other three
  // Store into "starting" and its index into the "distances" array into "ci"
  for (int i = 0; i < 3; i++) {
    for (int j = i+1; j < 4; j++) {
      distances[ci] = Coordinate3D_distance(co[i], co[j]);
      // Handle the case of two points being the "same"
      if (distances[ci] < DOUBLE_MARGIN) {
        Triangle3D single = (Triangle3D){b, c, d}; 
        if (i == 0){
          single = (Triangle3D) {b, c, d};
        } else if (i == 1) {
          single = (Triangle3D) {a, c, d};
        } else if (i == 2) {
          single = (Triangle3D) {a, b, d};
        }
        Object3D_append_triangle(o, single);
        return;
      }
      if (distances[ci] > (max_distance + DOUBLE_MARGIN)) {
        max_distance = distances[ci];
        starting = co[i];
        maxi = ci;
      }
      ci += 1;
    }
  }
 
  // Gather the points other than the starting one
  Coordinate3D tcoords[3] = {b, c, d};
  if (maxi == 3 || maxi == 4) {
    tcoords[0] = a; tcoords[1] = c; tcoords[2] = d;
  } else if (maxi == 5) {
    tcoords[0] = a; tcoords[1] = b; tcoords[2] = d;
  }

  // Put a triangle between starting point, and two closest to it
  Coordinate3D_get_closest_two(starting, tcoords, &closest1, &closest2, &farthest);
  Triangle3D t1 = (Triangle3D) {starting, closest1, closest2};
  Object3D_append_triangle(o, t1); 

  // For the remaining unused point, find two closest, and build second triangle
  tcoords[0] = closest1; tcoords[1] = closest2; tcoords[2] = starting;
  Coordinate3D_get_closest_two(farthest, tcoords, &closest1, &closest2, &starting);
  Triangle3D t2 = (Triangle3D) {farthest, closest1, closest2};
  Object3D_append_triangle(o, t2); 
}

// MY FUNCTIONS
/* Defined in 3d.h
 */
Scene3D* Scene3D_create(){
	Scene3D* newScene = malloc(sizeof(Scene3D));
	Object3D** newObjects = malloc(5 * sizeof(Object3D*));
	newScene->count = 0;
	newScene->size = 5;
	newScene->objects = newObjects;
	return newScene;
}

/* Defined in 3d.h
 */
void Scene3D_destroy(Scene3D* scene){
	int objectCount = scene->count;
	Object3D** objectList = scene->objects;
	for(int i = 0; i < objectCount; i++){
		long triangleCount = objectList[i]->count;
		Triangle3DNode* cur = objectList[i]->root;
		for(long j = 0; j < triangleCount; j++){
			Triangle3DNode* next = cur->next;
			free(cur);
			cur = next;
		}
		free(objectList[i]);
	}
	free(objectList);
	free(scene);
}

/* Defined in 3d.h
 */
void Scene3D_append(Scene3D* scene, Object3D* object){
	if(scene->count == scene->size){
		//resize
		scene->objects = realloc(scene->objects, scene->size * 2 * sizeof(Object3D*));
		scene->size = scene->size * 2;
	}
	scene->objects[scene->count] = object;
	scene->count += 1;
}

/* Defined in 3d.h
 */
void Scene3D_write_stl_text(Scene3D* scene, char* file_name){
	FILE* file;
	file = fopen(file_name, "w");
	fprintf(file, "solid scene\n");
	int objectCount = scene->count;
	Object3D** objectList = scene->objects;
	for(int i = 0; i < objectCount; i++){
		long triangleCount = objectList[i]->count;
		Triangle3DNode* cur = objectList[i]->root;
		for(long j = 0; j < triangleCount; j++){
			fprintf(file, "  facet normal 0.0 0.0 0.0\n");
			fprintf(file, "    outer loop\n");
			
			Triangle3D curTriangle = cur->triangle;
			Coordinate3D coord = curTriangle.a;
			fprintf(file, "    vertex %.5lf %.5lf %.5lf\n", coord.x, coord.y, coord.z);
			coord = curTriangle.b;
			fprintf(file, "    vertex %.5lf %.5lf %.5lf\n", coord.x, coord.y, coord.z);
			coord = curTriangle.c;
			fprintf(file, "    vertex %.5lf %.5lf %.5lf\n", coord.x, coord.y, coord.z);

			cur = cur->next;
			fprintf(file, "    endloop\n");
			fprintf(file, "  endfacet\n");

		}
	}
	fprintf(file, "endsolid scene\n");
	fclose(file);
}

/* This function will create and return a Triangle3D object filled with the Coordinate3D objects
 * passed to it.
 */
Triangle3D makeTriangle(Coordinate3D p1, Coordinate3D p2, Coordinate3D p3){
	struct Triangle3D triangle;
	triangle.a = p1;
	triangle.b = p2;
	triangle.c = p3;
	return triangle;
}

/* Defined in 3d.h
 */
Object3D* Object3D_create_pyramid(Coordinate3D origin,
		double width, double height, char* orientation){
		
	struct Coordinate3D pA;
	struct Coordinate3D pB;
	struct Coordinate3D pC;
	struct Coordinate3D pD;
	struct Coordinate3D pT;

	if((strcmp(orientation, "up") == 0) || (strcmp(orientation, "down") == 0)){
		pA.x = origin.x - (width / 2);
		pA.y = origin.y;
		pA.z = origin.z - (width / 2);

		pB.x = origin.x + (width / 2);
		pB.y = origin.y;
		pB.z = origin.z - (width / 2);

		pC.x = origin.x - (width / 2);
		pC.y = origin.y;
		pC.z = origin.z + (width / 2);

		pD.x = origin.x + (width / 2);
		pD.y = origin.y;
		pD.z = origin.z + (width / 2);

		pT.x = origin.x;
		pT.z = origin.z;
		if(strcmp(orientation, "up") == 0){
			pT.y = origin.y + height;
		} else{
			pT.y = origin.y - height;
		}

	} else if((strcmp(orientation, "forward") == 0) || (strcmp(orientation, "backward") == 0)){
		pA.x = origin.x - (width / 2);
		pA.y = origin.y - (width / 2);
		pA.z = origin.z;

		pB.x = origin.x - (width / 2);
		pB.y = origin.y + (width / 2);
		pB.z = origin.z;

		pC.x = origin.x + (width / 2);
		pC.y = origin.y - (width / 2);
		pC.z = origin.z;

		pD.x = origin.x + (width / 2);
		pD.y = origin.y + (width / 2);
		pD.z = origin.z;

		pT.x = origin.x;
		pT.y = origin.y;
		if(strcmp(orientation, "forward") == 0){
			pT.z = origin.z + height;
		} else{
			pT.z = origin.z - height;
		}
	} else{
		pA.x = origin.x;
		pA.y = origin.y + (width / 2);
		pA.z = origin.z + (width / 2);

		pB.x = origin.x;
		pB.y = origin.y + (width / 2);
		pB.z = origin.z - (width / 2);

		pC.x = origin.x;
		pC.y = origin.y - (width / 2);
		pC.z = origin.z + (width / 2);

		pD.x = origin.x;
		pD.y = origin.y - (width / 2);
		pD.z = origin.z - (width / 2);

		pT.y = origin.y;
		pT.z = origin.z;
		if(strcmp(orientation, "right") == 0){
			pT.x = origin.x + height;
		} else{
			pT.x = origin.x - height;
		}
	}

	Object3D* shape = malloc(sizeof(Object3D));
	shape->count = 6;
	Triangle3DNode* first = malloc(sizeof(Triangle3DNode));
	Triangle3DNode* cur = first;
	//(A,B,C)
	//cur->triangle = makeTriangle(pA, pB, pC);
	if((strcmp(orientation, "right") == 0) || (strcmp(orientation, "left") == 0)){
		cur->triangle = makeTriangle(pB, pD, pA);
	} else{
		cur->triangle = makeTriangle(pA, pB, pC);
	}
	//(D,C,B)
	Triangle3DNode* next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	//cur->triangle = makeTriangle(pD, pC, pB);
	if((strcmp(orientation, "right") == 0) || (strcmp(orientation, "left") == 0)){
		cur->triangle = makeTriangle(pC, pA, pD);
	} else{
		cur->triangle = makeTriangle(pD, pC, pB);
	}
	//(A,B,T)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(pA, pB, pT);
	//(B, D, T)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(pB, pD, pT);
	//(D,C,T)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(pD, pC, pT);
	//(C,A,T)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(pC, pA, pT);

	shape->root = first;

	return shape;

}


/* Defined in 3d.h
 */
Object3D* Object3D_create_cuboid(Coordinate3D origin,
		double width, double height, double depth){
	struct Coordinate3D p1;
	struct Coordinate3D p2;
	struct Coordinate3D p3;
	struct Coordinate3D p4;
	struct Coordinate3D p5;
	struct Coordinate3D p6;
	struct Coordinate3D p7;
	struct Coordinate3D p8;
	
	p1.x = origin.x - (width/2);
	p1.y = origin.y - (height/2);
	p1.z = origin.z - (depth/2);

	p2.x = origin.x - (width/2);
	p2.y = origin.y - (height/2);
	p2.z = origin.z + (depth/2);

	p3.x = origin.x - (width/2);
	p3.y = origin.y + (height/2);
	p3.z = origin.z - (depth/2);

	p4.x = origin.x - (width/2);
	p4.y = origin.y + (height/2);
	p4.z = origin.z + (depth/2);

	p5.x = origin.x + (width/2);
	p5.y = origin.y - (height/2);
	p5.z = origin.z - (depth/2);

	p6.x = origin.x + (width/2);
	p6.y = origin.y - (height/2);
	p6.z = origin.z + (depth/2);

	p7.x = origin.x + (width/2);
	p7.y = origin.y + (height/2);
	p7.z = origin.z - (depth/2);

	p8.x = origin.x + (width/2);
	p8.y = origin.y + (height/2);
	p8.z = origin.z + (depth/2);

	Object3D* shape = malloc(sizeof(Object3D));
	shape->count = 12;
	Triangle3DNode* first = malloc(sizeof(Triangle3DNode));
	Triangle3DNode* cur = first;
	//(1,2,3)
	cur->triangle = makeTriangle(p1, p2, p3);
	//(2,3,4)
	Triangle3DNode* next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p2, p3, p4);
	//(5,6,7)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p5, p6, p7);
	//(6,7,8)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p6, p7, p8);
	//(2,4,6)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p2, p4, p6);
	//(4,6,8)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p4, p6, p8);
	//(1,3,5)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p1, p3, p5);
	//(3,5,7)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p3, p5, p7);
	//(3,4,7)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p3, p4, p7);
	//(4,7,8)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p4, p7, p8);
	//(1,2,5)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p1, p2, p5);
	//(5,6,7)
	next = malloc(sizeof(Triangle3DNode));
	cur->next = next;
	cur = next;
	cur->triangle = makeTriangle(p2, p5, p6);
	
	shape->root = first;

	return shape;

}

void Scene3D_write_stl_binary(Scene3D* scene, char* file_name){
	FILE* file;
	file = fopen(file_name, "wb");
	char head[80] = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	fwrite(head, 1, 80, file);
	int objectCount = scene->count;
	Object3D** objectList = scene->objects;
	uint32_t totalTriangles = 0;
	for(int k = 0; k < objectCount; k++){
		totalTriangles += objectList[k]->count;
	}
	fwrite(&totalTriangles, 1, sizeof(totalTriangles), file);
	float zero = 0.0;
	for(int i = 0; i < objectCount; i++){
		long triangleCount = objectList[i]->count;
		Triangle3DNode* cur = objectList[i]->root;
		for(long j = 0; j < triangleCount; j++){
			//Normal Vector
			fwrite(&zero, 1, sizeof(zero), file);
			fwrite(&zero, 1, sizeof(zero), file);
			fwrite(&zero, 1, sizeof(zero), file);
			
			Triangle3D curTriangle = cur->triangle;
			//Vertex 1
			Coordinate3D coord = curTriangle.a;
			float x = (float) coord.x;
			float y = (float) coord.y;
			float z = (float) coord.z;
			fwrite(&x, 1, sizeof(x), file);
			fwrite(&y, 1, sizeof(y), file);
			fwrite(&z, 1, sizeof(z), file);
			//Vertex 2
			coord = curTriangle.b;
			x = (float) coord.x;
			y = (float) coord.y;
			z = (float) coord.z;
			fwrite(&x, 1, sizeof(x), file);
			fwrite(&y, 1, sizeof(y), file);
			fwrite(&z, 1, sizeof(z), file);

			//Vertex 3
			coord = curTriangle.c;
			x = (float) coord.x;
			y = (float) coord.y;
			z = (float) coord.z;
			fwrite(&x, 1, sizeof(x), file);
			fwrite(&y, 1, sizeof(y), file);
			fwrite(&z, 1, sizeof(z), file);


			cur = cur->next;
			uint16_t end = 0;
			fwrite(&end, 1, sizeof(end), file);
		}
	}
	fclose(file);

}
