## 3D Mesh Editor

The editor allows to alter mesh,  conveniently create complex shapes from simpler ones and precisely match mesh and drawings or photos of foil parts.

### Basic Scaling Controls

The controls Span, Chord/MAC, XPos are located on the Size tab of the Bottom Panel and can be applied even if the mesh is not show.  If part's mesh is complex, these controls alter each segment proportionally.

### Advanced Chord Editing Controls

When the 3D Mesh View is active, pressing 'space' key on the keyboard toggles the chord editor mode. Recall that the currently selected part is always displayed in yellow color.  When the chord editor mode is 'ON', 
the currently selected chord is displayed in green color. To change what chord is active, use the Up/Down Arrow keys or the a/z key pair.  Selection si cyclical. so pressing Up on the top chord makes the bottom chord active.  

When  the chord editor mode is 'ON', next to the Zoom slider, you'll see two additional sliders: LE and TE, allowing to move the leading and the trailing end of the selected chord, respectively,  in longitudinal direction. 
Foil can be oriented arbitrarily on the 3D View, however, it is recommended that on of 3 orthographic projection views are used for editing, with the keys t, s, f. 

####   Metasequoia Style
When you are in  the chord editor mode, and in addition in one of  3 orthographic projection views,  additional controls are displayed that allow to edit the chord Le and TE Metasequoia-style. In the Side or Top views, the two green boxes on top allow to move the leading and the  trailing ends. When in Front view, the green box on the right size allows to move the whole chord up or down. Metasequoia-style controls are activatedwhen you press with the mouse inside the box and then drag the pointer sideays (for X) or up/down (for Z).

Note that because span-segment parts contain multiple but always equally spaced chords, the ends are fixed and can not move in the spanwise direction. for wings and the fuselage the spanwise direction is Y (sideways) and for the Mast (aka strut) it is the vertical (Z) direction. That is why dragging  the mouse out of Z   Metasequoia box when Mast is the active part makes no effect. 

#### Altering Mesh Complexity

The simplest possible part has a mesh with only two chords. 
When you are in the chord editor mode, and in addition in one of  3 orthographic projection views, two additional controls, Ch+ and CH-, show up on top,  allowing to add or remove chords. Cubic interpolation is sued to reconstruct the leading and the trailing edge shape, so the part geometry  is inherited when the number of chords changes. 

#### Measuring Coordinates and Distances

When  in one of  3 orthographic projection views, you see 2wo sets of cross hair cursors and a moving circle centering one of the cross-hairs and touching the other one. This is measuring control. The data is displayed at the bottom of the panel. The dark green cross hair is the anchor and by default is at the fuselage nose. By clicking anywhere on the screen you can re-locate the anchor. The grey cross hair follows the mouse. This way you know the coordinates and distances by looking at the display at the bottom. 

### Background Image

To define part's shape precisely, more than a simple resuming tool is warranted. For that, a background image displaying the foil drawings or an accurate photo is a great help. An image can be copied into your PC's clipboard. Use the standard commands of your OS - it could be a  Ctrl-c or mouse menu selection on Windows PC. After that, activate the Mesh View and press Ctrl-c.  The image appears, rendered underneath the mesh.   Switch to the required view projection with t, s or f key, and then move and scale the mesh using the mouse wheel or dragging the right button. Combing the wheel or right button with the Ctrl key, you can scale or move  the BG image. Ctrl-i inverts the BG image colors. Ctrl-+ and Ctrl-- alter the contrast. Same can be done with Ctrl-PageUp and Ctrl-PageDown. Ctrl-Home and Ctrl-End controls BG image brightness. These commands are also available with button clicks on a separate pop-up dialog that can be activated when pressing on the Controls button located in the top right corner next to the Help sign/button. 


> Written with [StackEdit](https://stackedit.io/).
