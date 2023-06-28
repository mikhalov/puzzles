# Puzzles
Puzzles is an application that allows users to split an image into puzzle pieces and then assemble the pieces either manually or automatically. The application performs image processing to divide the image into puzzle pieces and then uses a specialized algorithm to put the pieces back together based on edge similarity.

## Table of Contents
* [Live Demo](#live-demo)
* [Features](#features)
* [Technologies Used](#technologies-used)
* [Algorithm for Automatic Assembly](#algorithm-for-automatic-assembly)
* [API Endpoints](#api-endpoints)

## Live Demo
You can try out a deployed version of this application on this website: [Puzzles](https://puzzles-fdnnnsri5q-lm.a.run.app/).

## Features
- **Image Splitting**: Upload an image and split it into a specified number of rows and columns.
- **Puzzle Assembly**: Assemble the pieces manually or automatically using an advanced edge-matching algorithm.
- **Image Conversion**: Convert images to Base64 and vice versa for easy handling and storage.

## Technologies Used
- **Java**: The application is written in Java which is well known for its robustness and versatility.
- **Lombok**: Reduces boilerplate code in Java.
- **Caffeine**: A high-performance Java-based caching library used for in-memory caching.
- **OpenCV**: An open-source computer vision and machine learning software library. Used for image processing tasks such as image splitting and template matching.
- **Docker**: Used for containerizing the application, ensuring that it runs the same regardless of the environment.
- **React.js**: Utilized for building the client-side user interface of the application.

# Algorithm for Automatic Assembly
The **automatic assembly** of puzzle pieces is handled by the ``PuzzleEdgeMatcher`` class. Here is a brief explanation of how it works:

**Edge Matching**: For each edge of a puzzle piece, the algorithm computes a similarity score with all other edges of other pieces. It uses template matching (``TM_CCOEFF_NORMED`` (Correlation coefficient) method in OpenCV) to calculate the similarity between edges.

**Edge Opposites**: The algorithm considers opposite edges while comparing. For example, the left edge of one piece is compared with the right edge of another.

**Thresholding**: The similarity score must exceed a specified threshold to consider the edges as matching.

**Caching**: Similarity scores are cached using Caffeine for faster subsequent lookups.

**Adjacent Pieces**: The algorithm looks for pieces whose edges match above the threshold and considers them adjacent. If multiple matches are found, the one with the highest probability is chosen.

## API Endpoints
> **Image Upload**

**URL**: ``/api/images``

**Method**: ``POST``

Payload Example:
```json
{
    "base64": "base64encodedImage",
    "mimeType": "image/png"
}
```
---
> **Get All Images**

**URL**: ``/api/images``

**Method**: ``GET``

---

> **Get Image by ID**

**URL**: ``/api/images/{id}``

**Method**: ``GET``

---
> **Get Puzzles**

**URL**: ``/api/puzzles?imageId={imageId}``

**Method**: ``GET``

---
> **Check if Puzzle is Completed**

**URL**: ``/api/puzzles/check-complete``

**Method**: ``POST``

Payload Example:
```json
{
    "sessionId": "sessionId",
    "entries": [
        {
            "location": {
                "x": 0,
                "y": 0
            },
            "base64": "base64_string1"
        },
        ...
    ]
}
```

---
> **Assemble Puzzle**
> 
**URL**: ``/api/assembler``

**Method**: ``POST``

Payload Example:
```json
{
    "sessionId": "sessionId",
    "entries": [
        {
            "location": {
                "x": 0,
                "y": 0
            },
            "base64": "base64_string1"
        },
        ...
    ]
}
```
