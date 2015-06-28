# Lights Out!

A test project that I made to test reagent and figwheel.

I've just made lein new figwheel lights-out -- --reagent

And voilà! A fresh new repo.

## Overview

A clone of an old physical electronic game, with more functionality.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

## This is an educational exercise

So please feel free to comment on the result!

## Things I can improve in no particular order

 - The default index.html text
 - The ability to publish in heroku
 - New tiles that add / remove rows and columns
 - Fixed / blank tiles? Mazes?
 - Fix randomtiles
 - Make legends from behaviours docstrings
 - Comment, refactor, clean the code
 - More intelligence in combination of tile types
 - General design
 - Hints
 - Cheats & bonus
 - 3D?
 - Ability to work offline?
 - Ability to store highscore intersessions? Interuser?
 - Is it easy to make this an app?
 - Can we generalize it majorly? Core.logic game master?
