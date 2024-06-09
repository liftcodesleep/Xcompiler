[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-9f69c29eadd1a2efcce9672406de9a39573de1bdf5953fef360cfc2c3f7d7205.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=9181959)
# Assignment X Documentation

Author: Jacob Lawrence

## Project Introduction

The goal of this project was to build a system that would take a program file written in x and interpret it as an executable program of bytecodes. The `ByteCodeLoader.java` parses and extracts each bytecode and its parameters as applicable into a `Program` object which is really an array of bytecodes. The `runTimeStack` handles the program counter to maintain the workflow of the program but is only accessed through `VirtualMachine` wrapper methods. 

## Execution and Development Environment

Java version: 17

Development Environment: VisualStudioCode

### Class Diagram

![image](https://user-images.githubusercontent.com/12554759/204123851-563a399f-f589-442d-b406-0c8bde3e0784.png) 

![image](https://user-images.githubusercontent.com/12554759/204123698-0be53761-976f-4ccc-9691-05b3044a3f48.png)

## Scope of work and Project Discussion

Task 1 was populating all of the `ByteCode.java` subclasses and their requisite methods.
Task 2 was populating `CodeTable.java` to match.
Task 3 was getting the basic i/o for `RunTimeStack.java` like pop push peek etc.
Task 4 was making wrapper methods to mimic those `RunTimeStack.java` methods in `VirtualMachine.java`
Task 5 was implementing `ByteCodeLoader.java`'s `loadCodes()` method to parse the file line by line and separate the lines into ByteCodes and their parameters.
Task 6 was implementing `resolveSymbolicAddresses()` but that required some ByteCode implementation so was interrupted by
Task 7 which was implementing `getBranchTarget()` methods as well as `setBranchTarget()` methods for the bytecodes subclasses that might have symbolic adresses.
Task 8 was handling `resolveSymbolicAddresses()`. My initial implementation had the `setBranchTarget()` methods as base class methods but I was told all additions to the base class needed to be universal and so I rewrote the method to cast to the appropriate subclass before invoking the `setBranchTarget` method.
Task 9 was copying the bytecode output test string formattings into the bytecode output methods (I won't pretend I didn't), and then adjusting them to be correct. This involved varying degrees of fleshing out each subclass implementation as some bytecodes needed to do their jobs to have the requisite data to output.
At this point the chronology gets a little muddy because things started moving quickly. I started working through failing test cases to flesh out requirements and bytecode implementations. The subclasses with labels were a little trickier, but soon came easily to me when I thought about similar MIPS functions. What x calls a CallCode is basically MIPS's JAL (Jump and Link). I really took to MIPS so a lot of it fell into place quickly for me as far as bytecode implementations. The last thing I resolved was `CallCode.java`'s `toString()` output. I ended up mimicking my `popFrame()` logic realizing I could iterate the same way but append a string instead of pop the stack.

![image](https://user-images.githubusercontent.com/12554759/204120377-a669a206-98e1-4b64-98ef-4f9fba08c135.png)

Pictured: the method I implemented to populate part of `CallCode.java`'s `toString()` output. It is a modified version of the method shown below.

![image](https://user-images.githubusercontent.com/12554759/204120294-42471d03-4230-41b5-b0a7-0b702d723998.png)

Pictured: My favorite method of the project, like a digital game of jenga it blows out `FrameTop` only for everything above it (the rest of the frame) to collapse such that the next iteration can repeat the process. I think it's cute and it's the sort of iterative and self enabling process high level languages afford us whereas the corresponding MIPS code for this would be... not cute.



## Results and Conclusions
I really liked this project but I also really liked MIPS and I felt this had a lot in common. The instructor said it would only be a minor head start but I think I might have struggled a lot more if I wasn't already familiar with some of these operations. Hard to say but some of the bytecodes that a lot of people asked me about were the ones I solved in my head about as quickly as I read what they were meant to do. 

### What I Learned
I learned a lot about error handling and catching. I didn't know much about it at all but thankfully a peer was able to help me in a big (non-illegal) way.  In all honestly I can't think of much else. I learned some specifics of Java implementations but as far as new logical methodologies it felt more like doing MIPS in another higher level language. A lot of the same concerns with managing the program counter and handling the stack and its frames properly, it felt like tread ground for a lot of it. I did learn some about string formatting but not enough yet that I feel comfortable with it. One super notable exception I just remembered is Java reflection, which is contained (as I understand it) in this line:

![image](https://user-images.githubusercontent.com/12554759/204120227-c11feb2d-774d-4351-a4ac-834ddc8c5567.png)

The purpose here being that we can add any new bytecodes just by updating `CodeTable.java` and implementing an appropriate bytecode subclass. I think there would be extra steps if the new code has a symbolic address, but that can be the next dev's problem.

### What I Could Do Better
If I had a better grasp of String formatting I would have had a lot less trouble with requirement 6, especially with the bytecodes that have variate paramaters. I'm not very confident in the way I allocated the responsibilities of each class. I put methods where I thought they made sense, but I'm a unsure about the placement of some of my methods. Another weakness I felt limited by was not understanding proper form in implementing my logic, or even any form at all. I find myself knowing what I want to do but struggling to know or find out how to do it. I think this is primarily due to a lack of "reps" in coding more intricate projects like this and I expect it will fade with practice.

### Challenges I Encountered
Getting sick for like 10 days was pretty lame for a start. Honestly without the extension I earned a solid 0, so thanks a ton for that. The biggest technical challenge for me was definitely building a model of the system in my head to try to understand what was going on and where and how I needed to modify and expand. I think I said the same thing last time but it really is the lengthiest segment of my work. I don't know if it's feasible, but I think it would help a lot if the code was available by the time the lectures going through it were given. I derived a lot of value from watching back through lecture recordings while following along with my own copy of the code, but when those lectures were happening I was pretty lost trying to keep up.
