<h4 align="center">A feature rich music player developed in Java, as part of an academic project.</h4>

<p align="center">
  <a href="#key-features">Key Features</a> •
  <a href="#how-to-use">How To Use</a> •
  <a href="#Techs-used">Techs Used</a> •
  <a href="#Feature-demonstration">Feature Demonstration</a> •
  <a href="#You-may-also-like">You may also like</a> •
  <a href="#license">License</a>
</p>

<p align="center" width="100%">
    <img src="/readmeFiles/fastDemo.apng">
</p>

## Key Features

* Plays music
  - All the essential controls you'd expect from a music player are implemented, namely play, stop, pause, mute, previous or next track, repeat, volume control, and more. 
  - This functionality uses the [a MediaPlayer](https://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html) javaFX class to implement all music control features.

* Collects file metaData
  - Available music metadata is presented in the app within collapsible menu's, this includes album art if available.

* "Drag & Drop" music drag system
  - This system simplifies adding new songs to a players playlist, a simple file drag retains and saves the filepath to the database.

* Remembers previously saved music and playlists
  - This project uses SQL server to store basic user information and individual playlists.
  - Upon login, if any playlists have been created by that specific user, they will be automatically loaded.

* Simple account creation and authentication system
  - Each user will need to create an account before first using the application.
  - System validates information before creating new users, no duplicate users with the same name are allowed, passwords must match, etc.

* Music / Playlist removal
  - The app allows the user to remove specific songs or whole playlists at the click of a button.
  - A confirmation menu will ask the user if he truly wishes to proceed.

## How To Use

- A user begins by creating a new account if he hasnt already, he then proceeds to login with this information.
- Upon login, if the user has previously created playlists, those playlists will be automatically loaded.
- Otherwise, a user will first need to create and name a playlist by clicking the appropriate buttons.
- Next he simply needs to drag and drop any music files to the music board.
- At this point, click any desired music to automatically start playing it.
- Available metadata such as album cover art, music genre, music name will be loaded into collapsible menu's so the user can consult them at his leisure.
- Click any of the music control options at any time, such as volume control, backtrack or fastforward in the song, mute, next or previous song, repeat and more.

## Techs used

This project uses the following technologies:

- Java
- Scenebuilder
- JavaFX
- Microsoft SQL Server
- MSSQL JDBC

## Feature demonstration

<p align="center" width="100%" controls>
https://user-images.githubusercontent.com/68301369/135676350-5c6570e3-a822-44d1-b886-f8dc3f80270f.mov
</p>

## You may also like

- [exampleApp](githubappUrlHere) - desc

## License

MIT

---

> GitHub [@hpenacho](https://github.com/hpenacho) &nbsp;&middot;&nbsp;
> LinkedIn [@hugopenacho](https://linkedin.com/in/hugopenacho/)

