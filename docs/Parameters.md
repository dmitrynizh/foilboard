## Foil Parameter File Syntax
There are two variants of  parameter file syntax: html and java-props.

### HTML Parameter File Syntax

Paremeter files with extensions .htm or .html are in HTML format.  
This format is compatible with Java Applet format and is structured as 
a well-formed  html page with APPLET on it. For example, at the time of writing this README, file np-rsx.html contains:
````
<HTML>
<HEAD>
  <TITLE>NP RSX Convertible prototype/concept T-Hydrofoil Simulator V 1.0</TITLE>
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
   where CHORD SPAN in meters THICKNESS CAMBER in %, ANGLE in degrees 
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

The comment embedded into this file briefly describes the syntax defining geometry of the Wing, Stab, Fuse and Mast parameters.  More on that below. 

If Applet compatibility is not needed, everything in an HTML file except a tag providing a set of PARAM tags can be stripped. Example:

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

would  suffice.

### Java props Parameter File Syntax

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

### Airfoil Parameter Definition Syntax

The generic format for the parameters Wing, Stab, Mast and Fuse is 

    FOIL CHORD SPAN THICKNESS CAMBER ANGLE POS*

where  CHORD  and SPAN are numeric values in meters,  THICKNESS and CAMBER are in %, ANGLE is in degrees. 

FOIL can be the name of an airfoil profile the tool recognizes, or the name of a file defining airfoil profile geometry.  Currently, the following airfoils names can be specified as FOIL field:

Aquila_9.3%
NACA_4_Series
NACA_64-814
NACA_63-412
Moth_Bladerider_V1
SD7084
SD8040

Out of these,  NACA_4_Series is the most versatile, defining a [NACA 4 series](http://airfoiltools.com/airfoil/naca4digit) foil with specified CHORD  THICKNESS  and CAMBER. Other airfoils from the list  are popular hydrofoiling craft profiles and define fixed CHORD  THICKNESS  and CAMBER.  

If the FOIL filed is not recognized as one of above, it is treated as file name.  Two formats of such files are supported - numeric and graphical/drawing. Examples of these from the distribution include

NACA_63A-410.foil  - foil definition in numeric format

air-chair-front-wing.jpg -   foil definition in graphical format

POS is optional field specifying distance of the part from the tip of the fuselage in meters.

When CHORD is a number, the shape of the part is rectangular plan-form. For complex geometry, use expanded format for CHORD as follows. 

Expanded CHORD is a semicolon-separated list of chord specs. Each chord spec  can be either L or L/X or L/X/Z, where L is chord length,  X is longitudinal relative offset from the prev chord, Z is same for vertical direction. All values are in meters.  Entering these by hand is a daunting task, automation should be used (TBD). 

> Written with [StackEdit](https://stackedit.io/).
