![Open in Codespaces](https://classroom.github.com/assets/open-in-codespaces-abfff4d4e15f9e1bd8274d9a39a0befe03a0632bb0f153d0ec72ff541cedbe34.svg)
# Assignment 3 Documentation

Author: Jacob Lawrence

## Project Introduction

In your own words, provide a brief overview of this project, highlighting the key components of the project that you will be reviewing in the Discussion section.

The goal of this project was to extend the implementation of a provided parser skeleton to handle new tokens following similar work done with the lexer, and to implement two new tree visitor classes to calculate and map the offset and depth of each node, and then render the nodes according to those values.

## Execution and Development Environment

Java version: 11 and 16
Development Environment: VSC and codespaces

### Class Diagram

![image](https://user-images.githubusercontent.com/12554759/198497745-e4292273-6d24-4ebd-aa53-f3e4977aa880.png)


## Scope of work and Project Discussion

From the initial commit at first there was some set up required, my lexer package needed to be copied over and updated to implement iLexer, and I needed to expand the enumSets to include our new tokens. After that I spent a lot of time digesting the code and trying to understand how it did what it does. Specifically Parser.java which now reads so transparently to me took a while to piece together. After that I turned to the grammar table which pretty much tells you exactly how to arrange the production piece by piece. I did spend a little more time on the range expression, but once I got a grip on what expect() was doing it fell into place.

Then came the visitors. I spent some time rewatching the lectures on them which were very helpful, but it really all clicked when I figured out what visitKids() was doing.
Once I understood that this function, paired with managing depth, was responsible for the traversal we discussed in class, everything else was just details. My initial implementation was almost spot on except for not shifting kids when necessary, and that took a few traces to figure out. I really saw the value of the Visitor Design Principle. It gives you a nice encapsulated but modular way to do work across your tree in a read only sort of way that avoids conflict between visitors.

```

  public void visitKids(AST t) {
    for (AST kid : t.getKids()) {
      kid.accept(this);
    }
    return;
  }

 ```
 
This is the real work horse.

## Results and Conclusions
Spent too much time debugging outputTest when I could and should have suspected my issues with string blocks were at fault and I could have moved on to visitors sooner.
A theme of these projects so far is I wish I'd started earlier, but this time I have time to spare. I think I did better to budget my time and to spend it efficiently; the outputTest rabbithole notwithstanding.

### What I Learned

I think most of my growth in this project came during the initial step of absorbing and digesting the code to understand the system I'd be working in.
The previous projects taught me to respect the value of passing that initial hurdle before diving in, and it paid off immediately because the iterative process of developing went so much more smoothly than previous assignments. To try to quantify what I made an effort to apply, I focused on deconstructing the project into the smallest logical pieces I could and interpreting them one by one. The initial skeleton came with a lot of "noise" code that, without understanding visitor, was initially opaque. Focusing on learning where I could and taking it piece by piece meant that once I'd learned what the building blocks were it was almost color coded. Not to say it was trivial in any sense, I spent many hours iterating, but there was consistent progress and occasional hiccups rather than the inverse. I liked how this project, as I put it, turned X into Scratch by simplifying components of the language into arbitrary elements of relationships. You don't need to know the details of the expression to know that an expression will follow for. I thought it was neat.

### What I Could Do Better

I really need to pursue some resources on best practices and naming conventions and similar. I did read up on it some, but I don't have much confidence in my code aesthetics or methodologies. I tried to name things according to how I thought of what they did or were. I also need to learn more about UML diagrams and how to translate code properly. I intend to keep better pace with the lectures moving forward by getting ahead of reading the code before the relevant lecture. Hopefully that way I find my questions in time for a lecture instead of after.

### Challenges I Encountered

The hard part was walking through the code and understanding what was happening, the implementation was not easy but it wasn't half the work I would say. My biggest challenge honestly was trying to work around my IDE refusing to acknowledge Text Blocks or use a JDK that did. I guess that's something else I could do better is knowing how to set my environment properly.
