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
#include <stdlib.h>
#include <string.h>
#include "3d.h"

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
