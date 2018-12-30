
## Guide To GUI Panels

![4 panels](my-LW-foil-and-ML70-15mph-forces.png)

### Top Left Panel

Has 3 Views: Edge, Forces, 3DMesh.  2 text rows the bottom, display Name, Make and Year parameters of the foil and, for convenience, current speed of the craft in knots, miles per hour, kilometers per hour and meters per second. 

#### Edge View

Displays crosscut (a.k.a. profile) of one of 4 hydrofoil parts - Wing, Stab, Mast, Fuse. The one displayed corresponds to the current selection on the top left pane.  Here is Moses Fluente wing in Edge View:

![Edge View Example](edge-view.png)

Has a Display sub-menu adding streamlines (moving or frozen), or airfoil geometry to the 2D view. The Edge View is the only Top Left Panel View preserved mainly "as is" from NASA FoilSim III.  

#### Forces View

Displays side view of the entire water-craft in flight, including the rider and the propulsion unit. The cyan-colored area is water, the deep blue colored area is air. The rider, depending on the configuration, is displayed either with a kite bar and lines, or with windsurfing sail, boom and mast. The position of the rider reflects the inputs - the tool computes the center of gravity location needed to stabilize the craft at given flight mode (speed, altitude), and moves the rider alone the board automatically. The vertical Zoom slider control on the left controls rendering scale. The Forces slider controls the scale of forces arrows. The Display menu toggles force arrow labels on/off. Here are a few snapshots:

![forces-view-1](forces-view-kite-far.png)
![forces-view-2](forces-view-kite-close.png)
![forces-view-3](forces-view-sail-far.png)
![forces-view-4](forces-view-sail-close.png)

#### 3D Mesh View


![3d-view-1](3d-view.png)

Displays a 3D rendering of the hydrofoil unit as 3d mesh.  The board is not rendered in the 3D Mesh View. The mesh model can be rotated with mouse and zoomed with a slider bar (no mouse-wheel-controlled zooming yet). The Display menu allows to select either perspective view or orthographic view.

### Bottom Left Panel

Has the following tabs: **Flight Shape Size ChoosePlot Options**
Many tabs have "parameter rows" - a group of a Label, Input textbox and a Slider bar with the text box and slider bar linked internally. Each parameter has a min and max limits, and out-of bounds inputs are autocorrected. 

#### Flight Tab

![Bottom Left Panel Flight Tab](panel-bl.png)

The tab contains two sections. The top section contains flight control inputs. The bottom section is Velocity Prediction Module (aka "VPP").

The top section allows to enter craft speed, altitude of the board above water, load (defined as rider weight plus craft weight minus kite/sail uplifting force) and craft pitch.
All these can be altered using either a text input box or a slider bar. When any of the sliders move, rider position changes automatically as required to balance the forces in the left-right, up-down plane.  

Note that the units displayed are Si - meters per second, Newtons - by default. Units can be changed with a selector located in the middle of the top right panel (it's near the red button Help).

The button "Pitch of Balanced Flight" is a simple goal-seeking function to find out the "right' craft pitch for given positions of the 4sliders. The checkbox "Autoadjust Pitch for Equilibrium" does it automatically each time when something changes (for example, each time when speed is altered). The checkbox "Autoload Strut/Mast" turns "on" a complex computational mode that finds out an angle of the strut/mast that is required to generate enough sideways force to counterbalance kite/sail side force (aka heeling force). 

The VPP module of the tool evaluates various performance goals, such as "what is the speed of minimal drag?". The VPP section of the left bottom panel has several rows, each with a solver button on the left and corresponding constraints on the right. Currently, there are 3 solvers supported, hence there are 3 such rows.

"Find Lowest TakeOff speed" goal seeks the speed required for the craft to transition from displacement travel (slow travel where the board is partly immersed in the water) into flight. The constraints specify the minimal lift required and maximum permissible drag. The lift required can be seen as the weight of the craft+rider minus the upward lift of kite/sail. The maximum permissible drag corresponds to maximum available forward pull from the propulsion element (kite, sail). These numbers must be different for kite-foiling and windfoiling! A kiter can greatly reduce the Lowest
TakeOff speed by sending the kite in fast motion across and thus generating significant upward lift and forward drive; the sail of a windfoiler can not reduce the weight at takeoff noticeably, and the drive generated at takeoff is rather much smaller than that of a fast moving kite, even when the windfoiler pumps vigorously. 


### Shape Tab

![Shape Tab](panel-bl-tab-shape.png)

Airfoil profile parameters of the currently selected Part (yellow rectangle on the Results Panel). On the top, a drop-down selector displays the name of the airfoil of the part - whatever was specified in the config file or was previously selected - and allows to alter it at any time.  By default the list of shapes includes NACA 4 Series and several fixed shapes such as Aquill9.3% and NACA 63-412. When FoilSimIII shapes are enabled with parameter USE_FOILSIM_FOILS set to true, the list also includes Joukowski, Ellipse and Flat Plate. These work OK in FoilBoard, but because these are 'analytical' foils, computation of Cl and Cd for them takes time and various plots may run slow. This can be optimized with some caching added,  but doing so was low priority because the emphasize was on foils used in real hydrofoils. 

The button "Import from File" does exactly what the name says -  allows to import a new airfoil from a file defining airfoil profile geometry.  

Below that button there are several rows of controls allowing to alter the shape.  For fixed-profile airfoils such as Aquilla shown on the image above, Camber and Thickness parameters are locked. 
Below the  parameter controls there are several buttons that help to quickly set a certain common shape. 
All such shapes are supported by NACA 4 family. For instance,  pressing "Flat Bottom" sets Shape to NACA 4 Series, Camber to 2.5 and Thickness  to 9. "Flat Plate" sets Camber to 0 and Thickness to 1. 
When FoilSimIII shapes are enabled, some of these buttons select FoilSim 'analytical' foils.

### Size Tab


![Size Tab](panel-bl-tab-size.png)

Only two parameter inputs with sliders are here: Chord and Span. The values are displayed according to the Units selection on the Results Panel, Si is the default. The Area input box allows to scale Chord and Span to given Area size. For parts with complex geometry Chord is  *Mean Chord* value. Aspect Rat is the value of aspect ration which is  Span/Chord. 

### Chose Plot Tab

![panel-bl-tab-choose](panel-bl-tab-choose.png)

This input tab  defines what plot is displayed on the right.  Below are screenshots of some of the selections, with commentary.

![panel-bl-tab-choose-whole-eq](panel-bl-tab-choose-whole-eq.png)
This shows equilibrium conditions at various speeds, with Rider Gravity Center (CG) position, total drag, board pitch and total Lift/Drag ratio shown. For trained eye these parameters tell a lot about hydrofoil craft performance. The vertical red line corresponds to takeoff speed.  

![panel-bl-tab-choose-whole-drag](panel-bl-tab-choose-whole-drag.png)
Drag of various Foil parts including Junction Drag for Wins-to-fuse connection and Spray Drag of the Mast at various board velocities. 
Typically, each foil has a sweet-spot where total drag is the  lowest.  Such velocity of minimal drag is higher than takeoff speed.  Normally, drag of Wing, Fuse and Stab contribute to that, each having a dip as shown. The 2nd Solver from the Flight tan finds this speed of minimal drag. 

![panel-bl-tab-choose-cl-cd-polar](panel-bl-tab-choose-cl-cd-polar.png)
Polar Plot for AoA in the range from -20 to 20. In addition to classic Cl/Cd plot for infinite geometry (aka *2D*,  aka *Profile*), a line for Aspect-ratio-corrected Cl vs  Profile Cd, and a line for that versus Induced drag corrected Cd are also shown. The later is what  defines foil part's actual performance. 

![panel-bl-tab-choose-drag](panel-bl-tab-choose-drag.png)
Part's total drag with various components of that plotted separately. Spray drag is computed only for the Mast part. 




> Written with [StackEdit](https://stackedit.io/).
