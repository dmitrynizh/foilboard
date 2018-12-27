## Kite/Wind Surf Hydrofoil Board Simulator

![example screenshot 1](docs/my-LW-foil-and-ML70-15mph-forces.png)


### Overview

This software tool computes forces on a recreational wind-powered hydrofoiling watercraft.  The watercraft is typically a small board with the rider standing on top of it. The board that is lifted in the air by a hydrofoil attached to the bottom of the board. The hydrofoil has 4 main parts: vertical strut secured to the bottom for the board that pierces the water surface, the lifting wing, a smaller, stabilizing wing, and a narrow, tube-like fuselage connecting the wings and the mast. 

The tool can aid in hydrofoil design and can be used to evaluate existing hydrofoils. Various performance goals, such as "what is the speed of minimal drag?" can be evaluated and solved. The results are presented in numeric, graphical, tabular formats. The author used this tool extensively and with great success in the design of DIY carbon windsurf hydrofoil built in 2017, 

https://1drv.ms/f/s!AhpYSQuCj3vrjHeKi4Bvpnp_r7kB 

which came out exactly as conceived, in part thanks to this tool's accurate predictions.

The forward driving force that propels the craft is generated either by a kite held by the rider and not connected to the board (the "Kite-Foiling" configuration) or a windsurfing sail with a wishbone boom held by the rider. The sail is normally connected to the board by a flexible joint ("Wind-Foiling" configuration).  

##### Side Note

> **Foiler's Jargon** Some of the jargon used by the kitesurfing and
windsurfing communities may be confusing to an outsider - thus, the activity of hydrofoil board riding is called "foiling" for short, and "foil" typically refers to the 4-part hydrofoil unit attached to the board. The vertical strut of the hydrofoil unit is usually called "mast", not to be confused with the windsurfing sail mast. "Kite-foiling" may be confused by some with kite-boarding on a foil-kite, but is reserved for kite hydrofoiling. More, "Wind-foiling" is a term that one might apply to both kite- and sail- board foiling, but typically refers strictly to the sailing variation. Finally, foiling now includes many other variations: SUP foiling with a paddle, wave/surf-foiling, lake/pump foiling, downwind foiling (with wind, but no sail or kite), tow foiling, wake foiling, e-Foiling (foilboard with electric motor propulsion). Ignoring the sail/kitebar rendering, this tool can be used to evaluate the foils used in each of these sports as well.

The tool computes hydrodynamic forces on all parts of the hydrofoil, finds the position of equilibrium for the rider, plots various parameters and predicts various steady conditions - minimal takeoff speed, cruising sweet-spot (speed of minimal drag), and maximum speed.

Broadly, there are two sets of input parameters: 

* parameters describing foil geometry. These often come from the init file which is a text/html file defining specific foil's geometry.
* parameters representing riding conditions.   

The user can alter the parameters using GUI with sliders, text boxes and check-boxes. The tool reacts to user actions instantly (in real time) and produces the results in real time. This instant feedback and reactivity is the main advantage of this tool; some other tools useful in hydrofoil design, such as XFLR5, require more efforts. 

This tool runs as standalone Java App (recommended) or as html-page embedded Java Applet (not recommended and difficult in modern browsers due to security restrictions). 
The tool's Java code has also been successfully converted to JavaScript so that it can run conveniently in modern browsers. This conversion was made possible using the SwingJS system. 


Note on SwingJS conversion: older versions of this tool are available online, see

http://www.dmitrynizh.com/swingjs/mywebsite/FoilBoard.htm

http://www.dmitrynizh.com/swingjs/mywebsite/WindFoiling.htm 

The latest/current version conversion is pending.  The original NASA FoilSim III has been also successfully converted to JS:

http://www.dmitrynizh.com/swingjs/mywebsite/FoilOrig.html

### Installing the Tool

No build is required to install the tool.  Simply unzip foilboard.zip into a convenient directory

### Running the Tool
You need command-line Java available on your computer. To check, 
open a command shell tool and type "java -version".   If available, it wil print ist version. Above 1.7 is OK. To run cd to the directory where foilboard.zip was unzipped, and enter

    java -jar foilboard.jar file-name.html

where ```file-name.html``` is a file providing hydrofoil parameters such as
any of the *.html files unziiped. Example:

    java -jar hydrofoil-sim.jar liquid-force-happy-foil.html

currently (Dec 2017) the following foil config files are provided:

Kiting:
````
F4kitefoil2015.html
F4kitefoil2015-pos-camb.html
Foil.html
MHL-lift-16.html
MikesLab-foil-Sonnys.html
air-chair.html
fone-freeride-600-ca-2016.html
fone-freeride-800-hy-2016.html
kfa-mk3.html
liquid-force-happy-foil.html
liquid-force-rocket-foil-prfiles-take2.html
liquid-force-rocket-foil.html
moses-fluente-2017.html
slingshot-LW-windfoil-2017.html
sroka-freeride.html
Tony-two-race-fins-symm.html
Tony-two-race-fins.html
````
Windsurfing:
````
F4windfoil-80cm.html
F4windfoil.html
horue-vini-2016-cruising.html
horue-vini-2016-lw-pos-stab-camber.html
horue-vini-2016-lw.html
horue-vini-2016-xtrem-lw.html
horue-vini-lw-canard-idea.html
np-rs-flight-al.html
np-rs-one-convertible.html
np-rsx.html
````

Dmitry's foil and ideas/variants for the future:
````
my-windfoil.htmln
my-windfoil-canard1.html
my-windfoil-canard2.html
my-windfoil-canard3.html
my-windfoil-mono-fwd-v.html
my-windfoil-mono-straight-wing-small-fuse.html
````

#### Foil Parameter File Syntax
There are two variants of  parameter file syntax: html and java-props.

##### HTML Parameter File Syntax

Paremeter files with extensions .htm or .html are in HTML format.  
This format is compatible with Java Applet format and is structured as 
a well-formed  html page with APPLET on it. For example, at the time of writing this README, file np-rsx.html contains:
````
<HTML>
<HEAD>
  <TITLE>Np RSX Convertible prototype/concept T-Hydrofoil Simulator V 1.0</TITLE>
  <META name="author" content="dmitrynizh"/>
  <META name="copyright" content="(c) 2017"/>
</HEAD>
<BODY>
<P><APPLET CODE="Foil.class" WIDTH='900' HEIGHT='600' ALIGN='bottom'>
  <PARAM NAME="Make"  VALUE="Neil Pryde"/>
  <PARAM NAME="Model" VALUE="RS:X Conv"/>
  <PARAM NAME="Year"  VALUE="2017"/>
<!-- Params Wing, Stab, Fuse, Mast have these VALUE formats: 
  (1) simple:  VALUE="FOIL CHORD SPAN THICKNESS CAMBER ANGLE POS*"
   where CHORD SPAN THICKNESS CAMBER ANGLE  - numeric values in meters
         FOIL - either one of built-tin names or name of data/image file
         POS* optional distance of part's leading edge (LE)  from the fuselage tip
  (2) multi-segmented, with equally-spaced chords
    where everything is as above except CHORD is ;-separated list of chord specs.
    Each chord spec  can be either L or L/X or L/X/Z, where L is chord length, 
    X is longitudinal relative offset from the prev chord, Z is same for vertical direction
 -->
  <PARAM NAME="Wing"  VALUE="NACA_4_Series 0.10  1.0  12 3.34  0"/>
  <PARAM NAME="Stab"  VALUE="NACA_4_Series 0.075 0.66 12 -3.34 -1"/>
  <PARAM NAME="Fuse"  VALUE="NACA_4_Series 0.88  0.02 4  0    0.0"/>
  <PARAM NAME="Mast"  VALUE="NACA_4_Series 0.12 1.0  12 0     0"/>
</APPLET></P>
</BODY>
</HTML>
````

The comment embedded into this file describes the syntax defining geometry of the Wing, Stab, Fuse and Mast parameters. If Applet compatibility is not needed, everything except a tag providing a set of PARAM tags can be stripped. Example:

````
<P>
  <PARAM NAME="Make"  VALUE="Neil Pryde"/>
  <PARAM NAME="Model" VALUE="RS:X Conv"/>
  <PARAM NAME="Year"  VALUE="2017"/>
  <PARAM NAME="Wing"  VALUE="NACA_4_Series 0.10  1.0  12  3.34  0"/>
  <PARAM NAME="Stab"  VALUE="NACA_4_Series 0.075 0.66 12 -3.34 -1"/>
  <PARAM NAME="Fuse"  VALUE="NACA_4_Series 0.88  0.02 4    0    0"/>
  <PARAM NAME="Mast"  VALUE="NACA_4_Series 0.12  1.0  12   0    0"/>
</P>
````


##### Java props Parameter File Syntax

Files with other extensions are parsed as Java properties. Example from above that might be stored as np-rsx.par 
````
Make: Neil Pryde
Model:RS:X Conv
Year: 2017
Wing: NACA_4_Series 0.10  1.0  12  3.34  0
Stab: NACA_4_Series 0.075 0.66 12 -3.34 -1
Fuse: NACA_4_Series 0.88  0.02 4    0    0
Mast: NACA_4_Series 0.12  1.0  12   0    0

````

### GUI Overview

The tool's main GUI window contains 4 rectangular panes/panels arranged on a 2x2 grid with the inputs located in the left-bottom rectangle. This configuration is inherited from NASA FoilSim tool, a java applet from which FoilBoard was originally derived. 

The purpose of each of the panels is as follows.

**Top Left Panel** displays the craft - either in 2d or 3d, the forces, or the 2D fluid flow around the airfoil section selected on the Top Right Panel.  At the bottom,  the Top Left Panel displays the craft name/make/model and flight speed in knots, mph, km/h and m/s.  The View menu, rendered  on top,  has 3 tabs/options: Edge, Forces, 3D Mesh.

**Top Right Panel** displays the results of computation in numeric format; contains 4 vertical columns WING, STAB, MAST, FUSE that aggregate the results for the main wing, the stabilizing wing, the strut/mast and the fuselage, respectively. 

**Bottom Left Panel** is a tabbed panel containing various inputs such as slider bars changing flight parameters, hydrofoil geometry, etc., and also the inputs of goal-seeking velocity prediction procedures (a.k.a. VPP').  See details below.

**Bottom Right Panel** is another tabbed panel displaying various plots and also craft's performance summary provided in either text or html formats. 

For details about Panels, click here:
[Guide To GUI Panels](docs/GUIPanels.md)
