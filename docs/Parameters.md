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

### Supported Parameters
All parameters except 4 airfoil-shaped ones - Wing, Stab, Mast and Fuse - hold simple values such as text description or a numeric value. The airfoil-shaped parameters have much more complex definitions and are described in a separate section of this guide. Other parameters include:

|Param | Alias if any | description|
|--|--|--|
| Make | |      manufacturer of the foil
| Model||    model name
|Year ||      year of production
| TYPE | CRAFT_TYPE | windfoil if starts with WIND. otherwise, kitefoil. Default is WIND
| BH | BOARD_THICKNESS | board thickness in m. Default: craft_type == WINDFOIL ? "0.1" : "0.06" |
| BL | BOARD_LENGTH | board length in m. Default: craft_type == WINDFOIL ? "2.35" : "1.25" |
| BW | BOARD_WEIGHT | weight-force of the board, N. Default:  craft_type == WINDFOIL ? "65" : "35" |
| RIG_WEIGHT | | weight-force of the sail rig (sail+mast_boom). Default: craft_type == WINDFOIL ? "100" : "0" |
| TKL || Required takeoff lift. This corresponds to effective weight at takeoff. Default: 735N |
| TKD || Required takeoff  drag. This corresponds to effective propulsion at takeoff. Default: 735N |
| CRL || Required cruising lift. This corresponds to effective weight at cruise speed. Default: craft_type == WINDFOIL ? "735" : "650" |
| RSL | TOTAL_WEIGHT | Total weight-force that defines required race lift. Default:  735N. |
| RSD || Max possible race drag. This normally corresponds to counterweight the rider can provide. Default: 245N |



### Airfoil Parameter Definition Syntax

The generic format for the parameters Wing, Stab, Mast and Fuse is 

    FOIL CHORD SPAN THICKNESS* CAMBER* ANGLE* POS*


where FOIL,  CHORD and SPAN are required,  the rest are optional. 

FOIL can be the name of an airfoil profile the tool recognizes, or the name of a file defining airfoil profile geometry.   

The list of the airfoils the tool recognizes is constantly growing. What's currently supported can be printed out with parameter LIST_FOILS.  At the moment, the list includes:

    Aquila_9.3%
    NACA_4_Series
    NACA_64-814
    NACA_63-412
    Moth_Bladerider_V1
    SD7084
    SD8040

Out of these,  NACA_4_Series is the most versatile, defining a [NACA 4 series](http://airfoiltools.com/airfoil/naca4digit) foil with specified  THICKNESS  and CAMBER and camber position at 40%.  Other airfoils from the list  are popular hydrofoiling craft profiles and define fixed CHORD  THICKNESS  and CAMBER.  

If the FOIL filed is not recognized as one of above, it is treated as file name.  Two formats of such files are supported - numeric and graphical/drawing. Examples of these from the distribution include

``NACA_63A-410.foil`` - foil definition in numeric format

``air-chair-front-wing.jpg`` -   foil definition in graphical format

When CHORD is a number, the shape of the part is rectangular plan-form. For complex geometry, use expanded format for CHORD as follows. 

Expanded CHORD is a semicolon-separated list of chord specs. For any part except the Mast the shape is assumed symmetrical, and the format describes the shape of a half: the 1st spec is for the root chord (center of the wing) and the last one is for the tip. For the mast, the 1st spec is for the bottom end. Each chord spec can be either L or L/X or L/X/Z, where L is chord length,  X is longitudinal relative offset from the prev chord, Z is same for vertical direction. All values are in meters.  

Example of an expanded wing chord spec:

    0.135/0;0.125/-0.02;0.115/-0.02;0.085/0.004;0.055/0.006

Example of an expanded mast chord spec:

    0.107/0;0.11/-0.0015;0.113/-0.0015;0.116/-0.0015;0.12/-0.0025

Entering these by hand is a daunting task, automation should be used to convert a 3D model data into this format,  Scripts exist for .obj and .mqo files (TBD).

SPAN is a numeric values in meters,  THICKNESS and CAMBER are in meters or in %, ANGLE is in degrees.  POS is an optional field specifying distance of the part from the tip of the fuselage in meters.

### Airfoil Representation
Internally, each airfoil known to the program is represented as a collection of arrays holding the Cl and Cd coefficients for given airfoil at predefined angles of attack. 
15-element arrays are for angles of attack from -28 to 28 degrees, step 4.  25 element arrays are for  for angles of attack from -24 to 24 step 2.  The values are cubic-interpolated to get Cl and Cd fast at given AoA. The interpolator is based on [Paul Breeuwsma coefficients](https://www.paulinternet.nl/?page=bicubic). This results in what are generally referred to as Catmull-Rom splines. For NACA 4 series, tables are provided for various values of thickness and camber, and are interpolated. The coefficients for the predefined airfoils were obtained from [Martin Hepperle JavaFoil](https://www.mh-aerotools.de/airfoils/javafoil.htm) tool with airfoil DAT files imported from  [Airfoil Tools](http://airfoiltools.com/).

#### JavaFoil App

When working in FoilBoard extensions, or simply evaluating prospective foils, use of Airfoil Tools website and Martin Hepperle JavaFoil tool is essential. Because running JavaFoil online as applet is difficult these days, Martin Hepperle jars needed to run JavaFoil  standalone - javafoil,jar and mhclasses.jar  - are included in foilboard.zip.  Note that these are covered separately by 
Martin Hepperle copyright/distribution notices which are different from MIT license. In particular, for-profit use of  javafoil,jar and mhclasses.jar is restricted:
 
> javafoil,jar,  mhclasses.jar : © 1996-2018 Martin Hepperle. You may use the data given in this document for your personal use. If you use this document for a publication, you have to cite the source. A publication of a recompilation of the given material is not allowed, if the resulting product is sold for more than the production costs.

Refer to https://www.mh-aerotools.de/airfoils/javafoil.htm for details.  

To run JavaFoil after  foilboard.zip.was unzipped, do:

    java -jar javafoil.jar MH.JavaFoil.JavaFoil


> Written with [StackEdit](https://stackedit.io/).
