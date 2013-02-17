Sequenced
=========

A silly little mathy game for Android. My first work with implementing a UI on Android.  The game began as a very simple game with one mode, but grew organically as I added more options.  Check it out on [Google Play](https://play.google.com/store/apps/details?id=org.petekinnecom.sequenced).

In the end, the [Model](https://github.com/petekinnecom/sequenced/blob/master/src/org/petekinnecom/sequenced/GameModel.java) code turned out clean and very understandable.

The [View](https://github.com/petekinnecom/sequenced/blob/master/src/org/petekinnecom/sequenced/GameView.java#L109) ended up taking on too many responsibilities.  The code could really use some refactoring to fully separate the logic of the different game modes away from the rendering code.
