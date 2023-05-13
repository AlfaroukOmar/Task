# Posts App

A simple Android app that displays a list of Movie with support for online and offline states, as well as pagination. Each Movie item contains a title and an image. The app contains two views: a list view and a details view.

## Features

- Movie item contains a title and image.
- Two views:
  - List view:
    - Each row contains a Movie title.
    - Supports pagination.
  - Details view:
    - Contains Movie title and image.
- Works in online and offline states.
- Unit tests.

## Technologies Used

- Kotlin
- Android Jetpack
  - ViewModel
  - LiveData
  - Room
  - Paging
  - Navigation
- Retrofit
- Glide
- Hilt (for dependency injection)
- JUnit and Mockito (for unit testing)
