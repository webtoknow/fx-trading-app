# Week 4 - Architecture

## What is a Single Page Application?

- is an app that works inside a browser
- does not require page reloading during use
- most resources (HTML + CSS + Scripts) are only loaded throughout the lifespan of application
- only data is transmitted back and forth
- no need to write code to render pages on the server
- easy to debug with Chrome - you can monitor network operations, investigate page elements and data associated with it
- can cache any local storage
- we are using this type of applications every day
- *e.g. : Gmail, Google Maps, GitHub*

## Angular 6

### About

- is a comprehensive JavaScript framework, used by developers to build web applications
- is built entirely in **TypeScript** - the ES6 version of JavaScript with support for type safety and tooling
- TypeScript must be 'transpiled' into JavaScript using the *tsc* compiler
- enable to write SPA (Single Page Applications) with ease
- comes with features like data-binding, change-detection, forms, router&navigation and http implementation

### ES6 and TyypeScript

- ES6 is the 6th version of the ECMA Script Programming Language, released in 2011
- adds many features intended to make large-scale software developement easier, including:
    - use of **const** and **let** instead of var
    - block-scoped variables and functions
    - arrow functions
    - default function parameters
    - class definition syntax
- TypeScript was created to statically identify constructs that are likely to be errors -> allow to make safe assumptions about state during execution

### Angular CLI

- is used by the generation of new angular projects
- everytime we start a new project, there is a bunch of files to be created -> Angular CLI can resolve that issue by generating all that files for us
- when it is done, we have a basic Angular project, run-able and containing all we need to continue
- after starting the application, a new port is opened, where a live server continues to run using **Webpack's dev server**

### Webpack

- is a popular mode bundler
- bundles application source code in convenient chunks
- loads that code from a server to browser
- takes in various assets (such as JavaScript, CSS and HTML) and then transforms these assets into a format that's convenient to consume through a browser
- based on a configure-driven approach and takes all assets in the project as dependencies -> treats the complete project as a dependency graph
- we can separate the source code into multiple files and import those files into the application to use the functionality contained in them
- module bundlers were built to bring this capability in a couple forms: 
    - by asynchronously loading modules and running them when they have finished loading
    - by combining all of the necessary files into a single JavaScript file that would be loaded via a **script** tag in the HTML.