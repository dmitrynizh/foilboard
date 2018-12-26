    Kite/Wind Surf Hydrofoil Board Simulator
    =================================

This software tool computes forces on a recreational wind-powered hydrofoiling watercraft.  The watercraft is typically a small board with the rider standing on top of it. The board that is lifted in the air by a hydrofoil attached to the bottom of the board. The hydrofoil has 4 main parts: vertical strut secured to the bottom for the board that pierces the water surface, the lifting wing, a smaller, stabilizing wing, and a narrow, tube-like fuselage connecting the wings and the mast. 

The tool can aid in hydrofoil design and can be used to evaluate existing hydrofoils. Various performance goals, such as "what is the speed of minimal drag?" can be evaluated and solved. The results are presented in numeric, graphical, tabular formats. The author used this tool extensively and with great success in the design of DIY carbon windsurf hydrofoil built in 2017, 

    https://1drv.ms/f/s!AhpYSQuCj3vrjHeKi4Bvpnp_r7kB 

which came out exactly as conceived, in part thanks to this tool's accurate predictions.

The forward driving force that propels the craft is generated either by a kite operated by the rider and not connected to the board (the "Kite-Foiling" configuration) or a windsurfing sail with a wishbone boom held by the rider. The sail is normally connected to the board by a flexible joint ("Wind-Foiling" configuration).  

Note: some of the jargon used by the kitesurfing and
windsurfing communities may be confusing to an outsider - thus, the activity of hydrofoil board riding is called "foiling" for short, and "foil" typically refers to the 4-part hydrofoil unit attached to the board. The vertical strut of the hydrofoil unit is usually called "mast", not to be confused with the windsurfing sail mast. "Kite-foiling" may be confused by some with kite-boarding on a foil-kite, but is reserved for kite hydrofoiling. More, "Wind-foiling" is a term that one might apply to both kite- and sail- board foiling, but typically refers strictly to the sailing variation. Finally, foiling now includes many other variations: SUP foiling with a paddle, wave/surf-foiling, lake/pump foiling, downwind foiling (with wind, but no sail or kite), tow foiling, wake foiling, e-Foiling (foilboard with electric motor propulsion). Ignoring the sail/kitebar rendering, this tool can be used to evaluate the foils used in each of these sports as well. 

The tool computes hydrodynamic forces on all parts of the hydrofoil, finds the position of equilibrium for the rider, plots various parameters and predicts various steady conditions - minimal takeoff speed, cruising sweet-spot (speed of minimal drag), and maximum speed.

Broadly, there are two sets of input parameters: 

* parameters describing foil geometry. These often come from the init file which is a text/html file defining specific foil's geometry.
* parameters representing riding conditions.   

The user can alter the parameters using GUI with sliders, text boxes and checkboxes. The tool reacts to user actions instantly (in real time) and produces the results in real time. This instant feedback and reactivity is the main advantage of this tool; some other tools useful in hydrofoil design, such as XFLR5, require more efforts. 

This tool runs as standalone Java App (recommended) or as html-page embedded Java Applet (not recommended and difficult in modern browsers due to security restrictions). 
The tool's Java code has also been successfully converted to JavaScript so that it can run conveniently in modern browsers. This conversion was made possible using the SwingJS system. 


Note on SwingJS conversion: older versions of this tool are available online, see

http://www.dmitrynizh.com/swingjs/mywebsite/FoilBoard.htm
http://www.dmitrynizh.com/swingjs/mywebsite/WindFoiling.htm 

The latest/current version conversion is pending.  The original NASA FoilSim III has been also successfully converted to JS:

http://www.dmitrynizh.com/swingjs/mywebsite/FoilOrig.html

Running the Tool

No build is required to run the tool. Follow these steps:

1. unzip foilboard.zip into a convenient directory
2. open a command shell tool and cd to that directory
2. start it with

    java -jar foilboard.jar file-name.html

where file-name.html is a file providing hydrofoil parameters such as
any of the *.html files unziiped. Example:

    java -jar hydrofoil-sim.jar liquid-force-happy-foil.html

currently (Dec 2017) the following foil config files are provided:

Kiting:

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

Windsurfing:
     
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

Dmitry's foil and ideas/variants for the future:

my-windfoil.htmln
my-windfoil-canard1.html
my-windfoil-canard2.html
my-windfoil-canard3.html
my-windfoil-mono-fwd-v.html
my-windfoil-mono-straight-wing-small-fuse.html



GUI

The tool's main GUI window contains 4 rectangular panes/panels arranged on a 2x2 grid with the inputs located in the left-bottom rectangle. This configuration is inherited from NASA FoilSim tool, a java applet from which FoilBoard was originally derived. 

The purpose of each of the panels is as follows.

Top Left: displays the craft - either in 2d or 3d, the forces, or the 2D fluid flow around the airfoil section selected on the Top Right panel. At the bottom, displays the craft name/make/model and flight speed in knots, mph, km/h and m/s.  The View menu has 3 selectable by mouse click tabs/options: Edge, Forces, 3D Mesh.

Bottom Left:  a muli-tab panel containing various inputs such as slider bars changing flight parameters, hydrofoil geometry, etc., and also the inputs of goal-seeking velocity prediction procedures (a.k.a. VPP'). 

Top Right: displays the results of computation in numeric format; contains 4 vertical columns WING, STAB, MAST, FUSE that aggregate the results for the main wing, the stabilizing wing, the strut/mast and the fuselage, respectively. 

Bottom Right:  multi-tabbed panel displaying various plots and also craft performance summary in either text or html formats. 

Three panels - all except the Top Right one - are multi-tabbed. Below is a short description of the most important tabs. 


Top Left Panel, Edge View

Displays crosscut of one of 4 hydrofoil parts - WING, STAB, MAST, FUSE - selectable on the top left pane. The current selection is in yellow.
The Edge View has a Display sub-menu adding streamlines (moving or frozen), or airfoil geometry to the 2D view. The Edge View is the only View option preserved from NASA FoilSim "as is", without changes. 

Top Left Panel, Forces View

Displays side view of the entire water-craft in flight including the rider and the propulsion unit. The cyan-colored area is water, the deep blue colored area is air. The rider, depending on the configuration, is displayed either with kite bar and lines or with windsurfing sail, boom and mast. The position of the rider reflects the inputs - the tool computes the center of gravity location needed to stabilize the craft at given flight mode (speed, altitude) moves the rider alone the board automatically. The vertical Zoom slider control on the left controls rendering scale. The Forces slider controls the scale of forces arrows. The Display menu toggles force arrow labels on/off.

Top Left Panel, 3D Mesh View

Displays a 3D rendering of the hydrofoil unit as 3d mesh.  The board is not rendered in the 3D Mesh View. The mesh model can be rotated with mouse and zoomed with a slider bar (no mouse-wheel-controlled zooming yet). The
Display menu allows to select either perspective view or orthographic view.

The tabbed bottom left pane has the following tabs: Flight Shape Size Choose Plot Options 

Flight Tab

Contains two sections. The top section contains flight control inputs. The bottom section is Velocity Prediction Module (aka "VPP").

The top section allows to enter craft speed, altitude of the board above water, load (defined as rider weight plus craft weight minus kite/sail uplifting force) and craft pitch.
All these can be altered using either a text input box or a slider bar. When any of the sliders move, rider position changes automatically as required to balance the forces in the left-right, up-down plane.  

Note that the units displayed are Si - meters per second, Newtons - by default. Units can be changed with a selector located in the middle of the top right panel (it's near the red button Help).

The button "Pitch of Balanced Flight" is a simple goal-seeking function to find out the "right' craft pitch for given positions of the 4sliders. The checkbox "Autoadjust Pitch for Equilibrium" does it automatically each time when something changes (for example, each time when speed is altered). The checkbox "Autoload Strut/Mast" turns "on" a complex computational mode that finds out an angle of the strut/mast that is required to generate enough sideways force to counterbalance kite/sail side force (aka heeling force). 

The VPP module of the tool evaluates various performance goals, such as "what is the speed of minimal drag?". The VPP section of the left bottom panel has several rows, each with a solver button on the left and corresponding constraints on the right. Currently, there are 3 solvers supported, hence there are 3 such rows.

"Find Lowest TakeOff speed" goal seeks the speed required for the craft to transition from displacement travel (slow travel where the board is partly immersed in the water) into flight. The constraints specify the minimal lift required and maximum permissible drag. The lift required can be seen as the weight of the craft+rider minus the upward lift of kite/sail. The maximum permissible drag corresponds to maximum available forward pull from the propulsion element (kite, sail). These numbers must be different for kite-foiling and windfoiling! A kiter can greatly reduce the Lowest
TakeOff speed by sending the kite in fast motion across and thus generating significant upward lift and forward drive; the sail of a windfoiler can not reduce the weight at takeoff noticeably, and the drive generated at takeoff is rather much smaller than that of a fast moving kite, even when the windfoiler pumps vigorously. 

<to be continued ...>
 
