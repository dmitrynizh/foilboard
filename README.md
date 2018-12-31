## Kite/Wind Surf Hydrofoil Board Simulator

![example screenshot 1](docs/my-LW-foil-and-ML70-15mph-forces.png)


### Overview

This software tool computes forces on a recreational wind-powered hydrofoiling watercraft.  The watercraft is typically a small board with the rider standing on top of it. The board that is lifted in the air by a hydrofoil attached to the bottom of the board. The hydrofoil has 4 main parts: vertical strut secured to the bottom for the board that pierces the water surface, the lifting wing, a smaller, stabilizing wing, and a narrow, tube-like fuselage connecting the wings and the mast. 

The tool can be used to evaluate existing hydrofoils and can aid in hydrofoil design.  The author used this tool extensively and with great success in the design of [DIY carbon windsurf hydrofoil](https://1drv.ms/f/s!AhpYSQuCj3vrjHeKi4Bvpnp_r7kB) built in 2017
which came out exactly as conceived, in part thanks to this tool's accurate predictions. 

Lots of efforts went into development and testing of analytical models. That said, because this software is hobby-facilitating tool created in spare time to aid in weekend DIY hobby activities (hydrofoil construction), the quality of code, coding style and UI are not top-notch; time was very limited for perfecting all that. Bugs certainly do exist, UI may look crude, and certain fragments of code may be kludgey. 

The tool computes hydrodynamic forces on all parts of the hydrofoil, finds the position of equilibrium for the rider, plots various parameters and predicts various steady conditions - minimal takeoff speed, cruising sweet-spot (speed of minimal drag), and maximum speed. Various performance goals, such as "what is the speed of minimal drag?" can be evaluated and solved.  The results are presented in numeric, graphical, tabular formats and change instantly reacting to GUI inputs.  

The forward driving force that propels the craft is generated either by a kite held by the rider and not connected to the board (the "Kite-Foiling" configuration) or a windsurfing sail with a wishbone boom held by the rider. The sail is normally connected to the board by a flexible joint ("Wind-Foiling" configuration).  

##### Side Note: Foiler's Jargon

> Some of the jargon used by the kitesurfing and windsurfing communities may be confusing to an outsider - thus, the activity of hydrofoil board riding is called "foiling" for short, and "foil" typically refers to the 4-part hydrofoil unit attached to the board. The vertical strut of the hydrofoil unit is usually called "mast", not to be confused with the windsurfing sail mast. "Kite-foiling" may be confused by some with kite-boarding on a foil-kite, but is reserved for kite hydrofoiling. More, "Wind-foiling" is a term that one might apply to both kite- and sail- board foiling, but typically refers strictly to the sailing variation. Finally, foiling now includes many other variations: SUP foiling with a paddle, wave/surf-foiling, lake/pump foiling, downwind foiling (with wind, but no sail or kite), tow foiling, wake foiling, e-Foiling (foilboard with electric motor propulsion). Ignoring the sail/kitebar rendering, this tool can be used to evaluate the foils used in each of these sports as well.

Broadly, there are two sets of input parameters: 

* parameters describing foil geometry. These often come from the init file which is a text/html file defining specific foil's geometry.
* parameters representing riding conditions.   

The user can alter these parameters using GUI with sliders, text boxes and check-boxes
located on input panels with multiple tabs.  The tool reacts to user actions instantly (in real time) and produces the results in real time. This instant feedback and reactivity is the main advantage of this tool; some other tools useful in hydrofoil design, such as XFLR5, require more efforts. 

### GUI Overview

The tool's main GUI window contains 4 rectangular panes/panels arranged on a 2x2 grid with the inputs located in the left-bottom rectangle. This configuration is inherited from NASA FoilSim tool, a java applet from which FoilBoard was originally derived. 

The purpose of each of the panels is as follows.

**Top Left Panel** displays the craft - either in 2d or 3d, the forces, or the 2D fluid flow around the airfoil section selected on the Top Right Panel.  At the bottom,  the Top Left Panel displays the craft name/make/model and flight speed in knots, mph, km/h and m/s.  The View menu, rendered  on top,  has 3 tabs/options: Edge, Forces, 3D Mesh.

**Top Right Panel** displays the results of computation in numeric format; contains 4 vertical columns WING, STAB, MAST, FUSE that aggregate the results for the main wing, the stabilizing wing, the strut/mast and the fuselage, respectively. 

**Bottom Left Panel** is a tabbed panel containing various inputs such as slider bars changing flight parameters, hydrofoil geometry, etc., and also the inputs of goal-seeking velocity prediction procedures (a.k.a. VPP').  See details below.

**Bottom Right Panel** is another tabbed panel displaying various plots and also craft's performance summary provided in either text or html formats. 

For details about Panels, click here: [Guide To GUI Panels](docs/GUIPanels.md).

This tool runs as standalone Java App (recommended) or as html-page embedded Java Applet (not recommended and often increasingly difficult in modern browsers due to security restrictions).  The tool's Java code has also been successfully converted to JavaScript so that it can run conveniently in modern browsers. This conversion was made possible using the SwingJS system. 


Note on SwingJS conversion: older versions of this tool are available online, see

http://www.dmitrynizh.com/swingjs/mywebsite/FoilBoard.htm

http://www.dmitrynizh.com/swingjs/mywebsite/WindFoiling.htm 

The latest/current version conversion is pending.  The original NASA FoilSim III has been also successfully converted to JS:

http://www.dmitrynizh.com/swingjs/mywebsite/FoilOrig.html

### Installing the Tool

No build is required to install the tool.  Simply unzip foilboard.zip into a convenient directory

### Running the Tool
You need command-line Java available on your computer. To check, 
open a command shell tool and type "java -version".   If available, it will print its version. Above 1.7 is OK. To run, cd to the directory where foilboard.zip was unzipped, and enter

    java -jar foilboard.jar file-name.html

where ```file-name.html``` is an optional parameter -  file path 'defining' specific foil, such as
any of the kite/\*.html or sail/\*.html files unzipped. Example:

    java -jar hydrofoil-sim.jar kite/liquid-force-happy-foil.html

Currently (Dec 2017), the following foil config files are provided

in folder **kite/**
````
f4-kitefoil-2015.html
mhl-lift-16.html
MikesLab-foil.html
air-chair.html
fone-freeride-600-ca-2016.html
fone-freeride-800-hy-2016.html
kfa-mk3.html
liquid-force-happy-foil.html
liquid-force-rocket-foil-prfiles-take2.html
liquid-force-rocket-foil.html
moses-fluente-2017.html
sroka-freeride.html
````
in folder **sail/**
````
f4-windfoil-80cm.html
f4-windfoil.html
horue-vini-2016-cruising.html
horue-vini-2016-lw-pos-stab-camber.html
horue-vini-2016-lw.html
horue-vini-2016-xtrem-lw.html
horue-vini-lw-canard-idea.html
np-rs-flight-al.html
np-rs-one-convertible.html
np-rsx.html
slingshot-LW-windfoil-2017.html
````

Ths folder also includes author's (Dmitry's) LW foil built in 2017, and also ideas/variants for the future:
````
my-windfoil.html
my-windfoil-canard1.html
my-windfoil-canard2.html
my-windfoil-canard3.html
my-windfoil-mono-fwd-v.html
my-windfoil-mono-straight-wing-small-fuse.html
````

### Foil Parameter File Syntax
There are two variants of  parameter file syntax: html and java-props.
Parameter files with extensions .htm or .html  - such as all those listed above - are in HTML format.  This format is compatible with Java Applet format and is structured as 
a well-formed  HTML page with APPLET tag enclosing a list of PARAMETER tags.

Files with other extensions are treated as Java properties file. This format is simple and compact and is easy to maintain and should be preferred when applet compatibility is not required. Example of a foil definition file content in Java properties format:

````
Make: Neil Pryde
Model:RS:X Conv
Year: 2017
Wing: NACA_4_Series 0.10  1.0  12  3.34  0
Stab: NACA_4_Series 0.075 0.66 12 -3.34 -1
Fuse: NACA_4_Series 0.88  0.02 4    0    0
Mast: NACA_4_Series 0.12  1.0  12   0    0

````
There is a long list of supported parameters, all of which are optional. Even the parameters describing Wing, Stab, Fuse and Mast can be dropped, in which case the tool runs with default geometry.  
Running the tool without parameter file is also possible - this:

    java -jar foilboard.jar

runs with all parameters being defaults.  The parameters can also be specified on a command line as -D options for java and must appear ahead of ``-jar foilboard.jar``:

    java -DTYPE=KITEFOIL -DBL=1.1 -DBW=25 -jar foilboard.jar 

Notice the option  ```-DTYPE=KITEFOIL``` above.  The default is windfoil, and a sail will be rendered as propulsion. If you prefer kite, put TYPE: KITEFOIL in the parameter file or DTYPE=KITEFOIL on the java command line as shown above. 

For details about parameters, click here: [Guide To Parameters](docs/Parameters.md).



