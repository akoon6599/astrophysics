# 2-Dimensional Orbit Simulator
This program was designed by Aila Mengden-Koon (Lincoln HS Year of 2023) as a teaching aid for Angie McVay's physics classes to help demonstrate the effect of gravitational forces.
This file exists as a user manual for students using this program in a class setting or for personal use, as well as a proof of ownership. The source code can be accessed at <https://github.com/akoon6599/astrophysics/> or mirrored at <https://github.com/EisbarGFX/astrophysics>.

This work is protected under the Creative Commons Non-Commercial License. Use and re-distribution are allowed for personal or educational usage only.

***


## How to Run
As this program is packaged as a single .jar file, it should be runnable no matter the Operating System by double clicking the file. If not, the following steps can be attempted:  
&nbsp;&nbsp;&nbsp;1. Open a terminal instance<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;i. On Windows: <kbd>⌘+F</kbd> -> cmd<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ii. On MacOS: <kbd>⌘+Space</kbd> -> Terminal<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;iii. On Linux: <kbd>Alt+Space</kbd> -> Terminal // Or specific program for your OS<br>
&nbsp;&nbsp;&nbsp;2. Navigate to the Downloads folder, or to where you moved the `astrophysics.jar` file<br>
&nbsp;&nbsp;&nbsp;3. Execute the command `java -jar astrophysics.jar`<br>

If the above does not work, check your Java installation with `java --version` and, if no result or a version below OpenJDK 18, install the newest version of Java according to the correct procedure for your OS.

***


## User Manual
The program begins with the `Main Menu` Screen.<br>
<u>![Main Menu Screen](https://user-images.githubusercontent.com/114428382/228426049-b829cd4e-d1ac-44b5-adac-6acef33048fc.png)</u>¹<br>
Current stellar bodies will be listed on the left with the<br>
&nbsp;&nbsp;&nbsp;1. Name<br>
&nbsp;&nbsp;&nbsp;2. Direction of movement expressed as an angle, where 0 degrees points rightwards along the screen and ±180 degrees points leftwards along the screen<br>
&nbsp;&nbsp;&nbsp;3. Velocity of movement expressed in kilometers/second, where this is 1:1 with reality<br>
&nbsp;&nbsp;&nbsp;4. Classification<br>
&nbsp;&nbsp;&nbsp;5. Mass expressed in 1e24kg, where this is 1:1 with reality<br>
&nbsp;&nbsp;&nbsp;6. Position expressed in a pair of (x,y) pixel coordinates, where:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;i. 2 pixels represent 1e6 kilometers in reality<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\— For example, the Earth's Aphelion of 152e6 km (positioned positively along the X axis) would be represented as (304,0)<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ii. and the position (0,0) represents the very center of the window<br>
&nbsp;&nbsp;&nbsp;7. "Anchor" attribute, where a true value represents a stationary body (Often a star or other orbital focus)<br>

displayed following it. Clicking on the button containing a body's name will allow you to edit the value of that body. This menu is near identical to the `Add Body` menu discussed below.

----
To the top right is the `Add Body` button, shown below.<br>
<u>![Add Body Screen](https://user-images.githubusercontent.com/114428382/228427628-2e59d1da-84ed-4c07-8a83-72d848778ca0.png)</u>¹<br>
Moving from the top down, the `Return to Menu` button will return you to the `Main Menu` screen, discarding any details entered, while the `Preview Movement` button will allow you to view a preview of the system's movement simulation, up to 100 steps.

The `Default Bodies` drop-down menu contains the bodies of our inner solar system as pre-configured options, and will auto-apply the parameters of any body upon selection.

The `Set Position` button will bring up a menu displaying other current bodies in the system, and allows you to click on a location to set the current body's position, and the `Orbit Focus` drop-down allows you to select an orbital focus for the current body, and subsequently set the perihelion (orbital distance) with the same scale as defined earlier for Position.

`Color` can be entered as a triplet of Red, Green, and Blue values, ranging from 0 to 255, separated by a comma. Can also be left blank for a default of black.

Checking `Display Velocity During Simulation` will display the object's velocity during all time during the simulation, allowing for accurate analysis of how velocity changes throughout an orbit.

`Finalize` will confirm the parameters entered and add the new body to the list in the `Main Menu`. If there are no errors, the window will be closed. Otherwise, the window will stay open and the body will not be created; you must make sure all fields are entered and formatted correctly before proceeding.

----
At the bottom left is a cog button. Clicking it will open the `Settings Menu`.<br>
<u>![Settings Menu](https://user-images.githubusercontent.com/114428382/228429339-91d339c1-00fe-4ed9-b965-241eaf14c9fa.png)</u>¹<br>
`Time Dilation` is the step size for each cycle frame. As noted, values between 0.01 and 2 are the recommended range. Higher values greatly decrease accuracy, while still relatively acceptable up to a value of 2, while low values will be more accurate but take many cycles to simulate much. Thus, a default and recommended value of 0.1 is set to achieve a relative balance.

`Length of Simulation` is how long the simulation will run in seconds; however, this is not exact, and will most likely run (up to) twice that length due to cycle frame delays.

`Delay Between Frames` is the minimum delay in ms between each cycle frame. This does not affect the physics except to determine the total number of steps in order to fit within the `Simulation Length` with the appropriate frame time. If you notice the simulation lags or jitters, try increasing this value slightly to give the computer more time to process each cycle frame.<br>
&nbsp;&nbsp;&nbsp;\— Worth noting is that if a frame takes longer than this value to execute due to calculation times, this value will be ignored and the next frame will begin immediately.
  
----
The simulation will begin on pressing the `Start Simulation` button in the `Main Menu Screen`. While the simulation is running, press the <kbd>Space</kbd> or <kbd>P</kbd> keys at any time to pause the simulation. A semi-transparent red label will appear at the top-left indicating that the simulation has been paused. Once the simulation is over, the orbital paths of each body will be displayed in the body's color and a `Exit to Menu` button will appear. Clicking it will return you to the `Main Menu Screen`, where you can edit the system or run the simulation again.
<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
¹ Note: If images are not appearing where the underlined text is, open either of the GitHub links at the top of this file to view them, or alternatively, open the corresponding menu in the program.
