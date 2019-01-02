/*-----------------------------------------------------------------------------
*
*   Kite/WindSurf Hydrofoil Simulator. 
*
*------------------------------------------------------------------------------ 
*
* (c) dmitrynizh 2016,2017.  This software is in the Public Domain.  It may
* be freely copied and used in non-commercial products, assuming
* proper credit to the author is given.  IT MAY NOT BE RESOLD.  If you
* want to use the software for commercial products, contact the
* author.  No copyright is claimed in the United States under Title
* 17, U. S. Code.  This software is provided "as is" without any
* warranty of any kind, either express, implied, or statutory,
* including, but not limited to, any warranty that the software will
* conform to specifications, any implied warranties of
* merchantability, fitness for a particular purpose, and freedom from
* infringement, and any warranty that the documentation will conform
* to the program, or any warranty that the software will be error
* free.  In no event the author be liable for any damages, including,
* but not limited to direct, indirect, special or consequential
* damages, arising out of, resulting from, or in any way connected
* with this software, whether or not based on warranty, contract, tort
* or otherwise, whether or not injury was sustained by persons or
* property or otherwise, and whether or not loss was sustained from,
* or arose out of the results of, or use of, the software or services
* provided hereunder.
*
*------------------------------------------------------------------------------
*
* Portions of this foil simulator are derived from "FoilSim III", see
* its copyright and header of FoilSim below. I tried to preserve the
* original look and feel of "FoilSim III" with its 4 panels of equal
* size arranged on a 2-by-2 grid, out of which left top is graphical,
* left bottom is inputs, right top is outputs and right bottom is for
* plots, data, gauges. Most code inherited from "FoilSim III" has been
* reachitected, varianles were renamned etc.
* 
* This simulator accurately computes lift and drag for a T-shape
* hydrofoil having a vertical strut (a.k.a. "mast"), a main wing, a
* horizontal stabilizer wing, and a fuselage connecting the wings and
* the mast. Various performance goals, such as "what is the speed of
* minimal drag?"  can be evaluated and solved. The results are
* presented in numeric, graphical, tabular formats, and can help to
* evaluate existing hydrofoils and predict properties and behavior of
* new design ideas. The author used this tool extensively in the
* design of DIY carbon windfoil built in 2017,
* https://1drv.ms/f/s!AhpYSQuCj3vrjHeKi4Bvpnp_r7kB which came out
* exactly as conceived, thanks to this tool's predictions.
*
*
*----From FoilSim.java, https://www.grc.nasa.gov/www/k-12/FoilSim------------- 

                      FoilSim III  - Airfoil  mode
   
                           A Java Applet
               to perform Kutta-Joukowski Airfoil analysis
                including drag from wind tunnel tests

                     Version 1.5b   - 3 Sep 13

                              Written by 

                               Tom Benson
                       NASA Glenn Research Center

                                 and
              
                               Anthony Vila
                          Vanderbilt University

                                NOTICE
This software is in the Public Domain.  It may be freely copied and used in
non-commercial products, assuming proper credit to the author is given.  IT
MAY NOT BE RESOLD.  If you want to use the software for commercial
products, contact the author.
No copyright is claimed in the United States under Title 17, U. S. Code.
This software is provided "as is" without any warranty of any kind, either
express, implied, or statutory, including, but not limited to, any warranty
that the software will conform to specifications, any implied warranties of
merchantability, fitness for a particular purpose, and freedom from
infringement, and any warranty that the documentation will conform to the
program, or any warranty that the software will be error free.
In no event shall NASA be liable for any damages, including, but not
limited to direct, indirect, special or consequential damages, arising out
of, resulting from, or in any way connected with this software, whether or
not based on warranty, contract, tort or otherwise, whether or not injury
was sustained by persons or property or otherwise, and whether or not loss
was sustained from, or arose out of the results of, or use of, the software
or services provided hereunder.
 
  New test -
             * include the drag
             * rename modules and change layout
             * change the color scheme on the plots
             * correct lift of ball from CurveBall
             * change pressure and velocity plots for stalled airfoil
             * add reynolds number calculation
               get moment coefficient - FoilSim IV
               put separation bubble on airfoil graphics - FoilSim IV

                                           TJB  22 Jun 10

  New Test B
           * -Build Drag Data Interpolator to incorporate drag results
             * -Implement interpolator into program (create drag output results textbar)
           * -get drag of elliptical foils

                                           AJV 7/14/10
  New Test C
           *  add drag plots and interpolated data
           *  move plot selection to input side
           *  add analysis panel - 
           *  make input and output buttons - not drop down
           *  add reynolds correction to airfoil - make optional
           *  add induced drag to drag calculation  - make optional
           *  add drag to gage output
           *  add smooth ball / rough ball option 
           *  release as FoilSim III
                                            TJB 29 July 10
           *  move metric/imperial units back on control panel - based on user inputs
           *  add some standard shapes to Shape input with buttons
                                            TJB 23 Nov 10
           *  fix up a symmetry problem for negative camber and drag 
           *  fix up a plotting problem for drag involving the induced drag
                                             TJB 29 Nov 10
           * change number of sig fig on graphs - small airfoils
                                             TJB 17 Feb 11
           * add volume of the wing to the printed output - option "Geometry"
                                             TJB 21 Mar 11
           * add Venus surface atmospherics (need to fix viscosity)
                                             TJB 20 Feb 13
             add relative humidty to the flight input
                                             TJB 3 Sep 13
                                                                                          
*/
//====  This section is for Swing 2 JS ==============================
//
// Note on SwingJS conversion: older versions of this tool are
// available online, see
// http://www.dmitrynizh.com/swingjs/mywebsite/FoilBoard.htm
// http://www.dmitrynizh.com/swingjs/mywebsite/WindFoiling.htm The
// latest/current version conversion is pending.  The original NASA
// FoilSim III has been also successfully converted to JS:
// http://www.dmitrynizh.com/swingjs/mywebsite/FoilOrig.html
//
//web_Ready
//web_AppletName= FoilBoard
//web_Description= FoilBoard = Wing + Fuse + Mast + Stab + Board!
//web_JavaVersion= https://github.com/dmitrynizh/foilboard
//web_AppletImage= no
//web_Category= foiling
//web_Date= $Date: 2017-01-01 06:08:18 +0000 (Sun, 01 Jan 2017) $
//web_Features= graphics, AWT-to-Swing
//===================================================================


// AWT Classes. No '.*', keep track of all links.
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.TextListener;
import java.awt.event.TextEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Swing classes. No '.*', keep track of all links.
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
/*test*/import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JApplet;

// Core Java Classes. No '.*', keep track of all links.
import java.lang.Math;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.io.File;


public class FoilBoard extends JApplet {

  // really, a struct
  static public class Point2D {
    double x, y;
    Point2D (double x, double y) {
      this.x = x;
      this.y = y;
    }
  }
  // really, a struct
  static public class Point3D {
    double x, y, z; // length, span, heigth. tail is + from nose, left is -, right is +.
    Point3D (double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }

  class Part {
    /**/ String name; Foil foil; double xpos; double chord; double span; double thickness; double camber; double aoa;
    Part(String name, Foil foil, double xpos, double chord, double span, double thickness, double camber, double aoa) {
      this.name = name;
      this.foil = foil;
      this.xpos = xpos;
      this.chord = chord;
      this.span = span;
      this.thickness = thickness;
      this.camber = camber;
      this.aoa = aoa;
      compute_aux_params();
    }
    
    void compute_aux_params () {
      area = span *chord;
      aspect_rat = span/chord;
      spanfac = (int)(2.0*fact*aspect_rat*.3535);
    }

    String[] chord_spec;
    // chord x-offset for complex geometry
    double chord_xoffs;
    double chord_zoffs;
    // coeff for induced drag. it is 1 for elliptical wings;
    // for rectangle form Ci_eff is 0.85. 
    double Ci_eff = 0.8; 
    double xoff_tip;
    Point3D[] mesh_LE;
    Point3D[] mesh_TE;
    Point3D[] mesh_LE_t; // after 3d transform
    Point3D[] mesh_TE_t; // after 3d transform

    double velocity, reynolds; // current cached velocity and reynolds number

    // these are forces in Newtons
    double lift, 
      drag, // fprofile+induced+junction+spray
      drag_profile, drag_aux, drag_junc, drag_spray, // drag components
      moment;

    double 
    cl, cd, cm, cd_profile, cd_aux;
    
    double[] t_Cd, t_Cl; // when this.foil, this.camber or this.thickness change, recompute!

    double area; // sq m

    // aspect ratio and span factor
    double aspect_rat; int spanfac;

    String foil_descr () {
      return this.foil.getDescr(thickness, camber);
    }

    void print (String header, JTextArea out) {
      if (header != null) {
        out.append("\n\n *** " + header + " ***");
      }
      out.append(toString());
    }

    @Override
    public String toString () {
      StringBuffer out = new StringBuffer();
      out.append(foil.print_line);      
      if (!foil_is_cylinder_or_ball(current_part.foil)) {
        if (lunits == IMPERIAL) {
          out.append( "\n Chord = " + filter3(chord*12) + " inches.");
          out.append( "\n Span = " + filter3(span*12) + " inches.");
          out.append( "\n Surface Area = " + filter3(span*chord*144) + " sq inches.");
          out.append( "\n Thickness = " + filter3(0.01 * thickness * chord * 12) + " inches");
          out.append( " or " + filter3(thickness) + " % of chord ," );
          out.append( "\n Camber = " + filter3(camber) + " % chord ," );
          out.append( "\n Angle of attack = " + filter3(aoa) + " degrees ," );
          out.append( "\n Position LE at " + filter3(xpos) + " inches aft fuse LE" );
        } else {
          out.append( "\n Chord = " + filter3(chord*100) + " cm.");
          out.append( "\n Span = " + filter3(span*100) + " cm.");
          out.append( "\n Surface Area = " + filter3(span*chord*100*100) + " sq cm.");
          out.append( "\n Thickness = " + filter3(0.01 * thickness * chord * 100) + " cm");
          out.append( " or " + filter3(thickness) + " % of chord ," );
          out.append( "\n Camber = " + filter3(camber) + " % chord ," );
          out.append( "\n Angle of attack = " + filter3(aoa) + " degrees ," );
          if (this == stab && stab_aoa_correction)
            out.append( "\n   Effective AoA due to Wing flow influence: " + filter3(effective_aoa()) + "," );
          out.append( "\n Position LE at " + filter3(xpos*100) + " cm aft fuse LE" );
        }
      } else { // cylinder or ball
        // out.append( "\n Spin  = " + filter3(spin*60.0) );
        // out.append( " rpm ," );
        // out.append( " Radius = " + filter3(radius) );
        // if (lunits == IMPERIAL) out.append( " ft ," );
        // else /*METRIC*/         out.append( " m ," );
        out.append( "\n Span = " + filter3(span) );
        if (lunits == IMPERIAL) out.append( " ft." );
        else /*METRIC*/          out.append( " m." );
      }
      return out.toString();
    }

    // NO-OP now!!!
    void save_state () { }

    public String toDefString () {
      String def = foil.descr.replace(" ", "_") + " " +
        (chord_spec.length == 1 ? chord : stringArrJoin(chord_spec, ";")) + " " + 
        span + " " + thickness + " " + camber + " " + aoa + " " + xpos;
      // System.out.println("-- part def: " + def);
      return def;
    }

  } // Part


  boolean runAsApplication = false; // main() sets this to true
  static Properties props;
  static JFrame frame;
  /** 
   * to run this applet as java application. 
   * expects file name as 1st argument, either applet html file or
   * a propeprties file
   *
   * @j2sNative
   *
   * // main() not supported in JS 
   * var dummy;
   *  
   */ 
  public static void main (String argv[]) {
    if (argv.length > 0) { // read params as props from file
      String params_file = argv[0];
      File file = new File(params_file);
      String path  = file.getPath();
      int path_last_sep_idx = path.lastIndexOf(File.separator);
      String dirpath = path_last_sep_idx > -1 ? path.substring(0, path_last_sep_idx+1) : "";

      if (params_file.endsWith(".html") || params_file.endsWith(".htm")) { // apllet html file
        try {
          javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
          javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          org.w3c.dom.Document doc = dBuilder.parse(file);
          doc.getDocumentElement().normalize();
          org.w3c.dom.NodeList nList = doc.getElementsByTagName("PARAM");
          props = new Properties();
          for (int temp = 0; temp < nList.getLength(); temp++) {
            org.w3c.dom.Node nNode = nList.item(temp);
            if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
              org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
              String name = eElement.getAttribute("NAME");
              String value = eElement.getAttribute("VALUE");
              props.setProperty(name, value);
            }
          }
        } catch (Exception e) {
          System.out.println("-- e: " + e);
        }
        
      } else { // java props syntax 
        try { (props = new Properties()).load(FoilBoard.class.getClassLoader().getResourceAsStream(params_file)); } 
        catch (Exception ex) { ex.printStackTrace(); }
      }
      props.setProperty("__def_file_dirpath",  dirpath);
    }
        
    frame = new JFrame();
    final FoilBoard  foilboard = new FoilBoard();
    foilboard.runAsApplication = true; // not as  Applet
    frame.getContentPane().add(foilboard);
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
        public void windowClosing(java.awt.event.WindowEvent we) {
          foilboard.stop();
          foilboard.destroy();
          System.exit(0);
        }
      });
    frame.pack();
    frame.setSize(Integer.parseInt(getProperty("WIDTH", "1000")), Integer.parseInt(getProperty("HEIGHT", "700")));
    foilboard.init();

    // foilboard.out.plot.loadPlot();
    // foilboard.con.recomp_all_parts();
    // foilboard.vpp.steady_flight_at_given_speed(5, 0);

    foilboard.start();
    frame.setVisible(true);
  }

  boolean on_cg_plotting = false;

  static boolean inited = false;

  static String stringArrJoin(String[] arr, String separator) {
    if (null == arr || 0 == arr.length) return "";
    StringBuilder sb = new StringBuilder(256);
    sb.append(arr[0]);
    for (int i = 1; i < arr.length; i++) sb.append(separator).append(arr[i]);
    return sb.toString();
  }
  
  class Panel extends JPanel {

    public Panel (LayoutManager layout) {
      super(layout);
    }

    public Panel () {
      super();
    }

    public Component add (Component comp) {
      super.add(comp);
      return comp;
    }

    // LoadPanel() to ensure Tab panes autimatically refresh when switched to
    public void loadPanel () {
      System.out.println("warning: unimplemented loadPanel() in " + this);
    }
    // prvent reentering loadPanel...
    // two ways to do it. new way is by returning from loadPanel:
    // if (on_loadPanel) return; on_loadPanel = true; <code>; on_loadPanel = false;
    // old way is with checks in each adjustmentlistener
    protected boolean on_loadPanel;

    // a little sty;e helper
    Button new_button (String title) {
      Button b = new Button(title);
      b.setBackground(Color.white);
      b.setForeground(Color.blue);
      return b;
    }
  } // Panel

  class Canvas extends JPanel {

    Canvas() {

      this.addMouseListener(new MouseListener() {
          @SuppressWarnings("deprecation")
          @Override
          public void mouseReleased (MouseEvent e) {
            // System.out.println(":MOUSE_RELEASED_EVENT:");
            mouseUp(new Event(e.getSource(), Event.MOUSE_UP, e), e.getX(),
                    e.getY());
          }
          @SuppressWarnings("deprecation")
          @Override
          public void mousePressed (MouseEvent e) {
            mouseDown(new Event(e.getSource(), Event.MOUSE_DOWN, e), e.getX(),
                      e.getY());
          }
          @Override
          public void mouseExited (MouseEvent e) {
            // System.out.println(":MOUSE_EXITED_EVENT:");
          }
          @Override
          public void mouseEntered (MouseEvent e) {
            // System.out.println(":MOUSE_ENTER_EVENT:");
          }
          @Override
          public void mouseClicked (MouseEvent e) {
            // System.out.println(":MOUSE_CLICK_EVENT:");
          }
        });
      this.addMouseMotionListener(new MouseMotionListener() {
          @SuppressWarnings("deprecation")
          public void mouseDragged (MouseEvent e) {
            mouseDrag(new Event(e.getSource(), Event.MOUSE_DRAG, e), e.getX(),
                      e.getY());
          }
          @SuppressWarnings("deprecation")
          public void mouseMoved (MouseEvent e) {
            mouseMove(new Event(e.getSource(), Event.MOUSE_MOVE, e), e.getX(),
                      e.getY());
          }

        });
    }

    @Override 
    public void paint (Graphics g) { update(g); }


    private boolean notified;
    @Override
    public void update (Graphics g) {
      if (!notified)
        System.out.println("neither paint(g) nor update(g) is implemented for "
                           + this);
      notified = true;
    }
  }

  class Button extends JButton {
    public Button(String text) { 
      super(text); 
      this.setMargin(new Insets(0, 0, 0, 0));
    }
  }

  static int next_foil_id = 0;
  static Hashtable foils = new Hashtable();
  void setFoil (Foil foil) {
    // if (current_part == xxxx) new Exception(("-- setFoil foil: " + foil)).printStackTrace(System.out);
    current_part.foil = foil;
  }

  void setFoil (String name) {
    // if (current_part == stab) new Exception(("-- setFoil stab name: " + name)).printStackTrace(System.out);
    current_part.foil = (Foil)foils.get(name);
  }

  // Not jargon "Foil", but CFD foil-profile
  class Foil { 
    int id; 
    String descr;
    String print_line;

    // reynolds_correction_fixpt: variable used by adjust_dragco
    double reynolds_correction_fixpt = 50000; // value used by legacy FS3 foils (Joukowski)

    double[] points_x;
    double[] points_y;
    Point2D[] geometry;
    Point2D[] camber_line;

    Foil () { }
    Foil (String descr, String print_line) {
      this.id = next_foil_id++;
      this.descr = descr;
      this.print_line = print_line;
      foils.put(descr, this);
    }

    @Override 
    public String toString () {
      return "{Foil id: " + id + " name: " + descr + "}";
    }

    double get_Cl (double effaoa) {
      return solver.compute_Cl_Kutta(effaoa);
    }

    // we now side effect into current_part, so that various drag factors are stored
    double get_Cd (double cldin, double effaoa, double thickness, double camber) {
      current_part.cd_profile = getCdragIdeal(cldin, effaoa, thickness, camber);
      // (1) Reynolds Correction, multipicative
      current_part.cd_profile = adjust_dragco(current_part.cd_profile, cldin, thickness);
      // (2) AR/induced drag correction, additive
      current_part.cd_aux = getCdragAux(cldin, thickness);
      return current_part.cd = current_part.cd_profile + current_part.cd_aux;
    }

    double getCdragIdeal (double cldin, double effaoa, double thickness, double camber) {
      return solver.getCdragAnalytical(cldin, effaoa, thickness, camber);
    }

    // this is mainly Reynolds Correction
    double adjust_dragco (double dragco, double cldin, double thickness) {
      if (re_corr) {    // reynolds correction
        // examples:
        // (expt (/ 300000 500000.0) 0.11) 0.9453587266360274
        // (expt (/ 300000 200000.0) 0.11) 1.0456107473217757
        // (expt (/ 300000 100000.0) 0.11) 1.1284526429021564
        // (expt (/ 700000 300000.0) 0.11) 1.097684284266364
        dragco = dragco * Math.pow((reynolds_correction_fixpt/current_part.reynolds),.11);
        }

      return dragco;
    }

    // this is mainly induced drag 
    double getCdragAux (double cldin, double thickness) {

      if (induced_drag_on == false)
        // done
        return 0;
        
      // current_part.Ci_eff is induced drag coefficient factor' for rectangle = .85, elliptic distr is 1 or so
      // adjust for strut in special way...
      // this must include wave and spray but the waw we calculate induced drag likely results in higher
      // .. drag Cl... stiil proper estimate for strut is:
      // (1) adjust area below to be 1/3 (or what 1-height/100 is) where drag is computed, use 1/2 of this section drag plust 
      // (3) wave & spray drag as
      // 0.24 * q0_SI * strut.thickness * strut.thickness. See "Full measurm... page 8"
      double k = 3.1415926 * 
        Math.max(0.03, current_part.aspect_rat) // otherwise K shrinks causing Cd to grow too much for ultra low aspect stuff like fuse (ar = 0.025 or less):
        * current_part.Ci_eff;    // (/(* 0.1 0.1)(* 3.14 0.025 0.8)) vs  (/(* 0.1 0.1)(* 3.14 0.1 0.8)) and ar=1: (/(* 0.1 0.1)(* 3.14 0.1 0.8))
      
      if (current_part == strut) {
        // (1) corect the aspect_rat used above for actual mast AR, which is a portio of it submerged AR
        k *= (1-alt/100);
        // increase k by 2 because this is bottom end only, the top
        // end does not have induced drag.  Instead, for top, wave and
        // spray adjustment apply,  see loadOutPanel()
        k *= 2; 
        // note that for 50% submerged mast teh net effect of the
        // above two corrections is 0.5*2=1 
      }
      return (cldin * cldin)/k;
    }

    double getCmoment (double effaoa) {
      return 0;
    }


    String getDescr (double thickness, double camber) {
      return descr;
    }

    void adjust_foil_shape_in_tab () {
      in.toggle_shp_tab();
      in.shp.set_camber_and_thickness_controls(true);
    }
    String genReportText (double thickness, double camber) {
      return "\n" + print_line;
    }

    boolean tabulated_coeffs () { return false; }
  } // class Foil

  class RoundFoil extends Foil {
    RoundFoil (String descr, String print_line) {
      //super(descr, print_line);
      this.id = next_foil_id++;
      this.descr = descr;
      this.print_line = print_line;

    }

    // fix later
    // double get_Cl (double effaoa) {
    // }

    @Override
    double getCdragIdeal (double cldin, double effaoa, double thickness, double camber) {
      return solver.getCdragRound(cldin, effaoa, thickness, camber);
    }
    // probably need not overload as adjust_dragco is not invoked in the above getCdrag
    @Override
    public double adjust_dragco (double dragco, double cldin, double thickness) { return dragco; }

    // switch to Cylinder's tab
    @Override
    void adjust_foil_shape_in_tab () {
      in.toggle_cyl_tab();
    }

  } // class RoundFoil


  class NACA4Foil extends Foil {
    NACA4Foil (String descr, String print_line) {
      super(descr, print_line);
      reynolds_correction_fixpt = 300000;
    }

    @Override
    double get_Cl (double effaoa) {
      if (current_part.t_Cl == null)
        current_part.t_Cl = solver.compute_t_Cl(solver.t_lift_NACA4, current_part.thickness, current_part.camber);
      return solver.ci15(current_part.t_Cl, effaoa);
    }

    @Override
    double getCdragIdeal (double cldin, double effaoa, double thickness, double camber) {
      // todo: why cache like above does not work?
      return solver.ci15_from_javafoil_data(solver.t_drag_NACA4, effaoa, thickness, camber);
    }

    @Override
    boolean tabulated_coeffs () { return true; }

    @Override
    String getDescr (double thickness, double camber) { 
      String thickness_str = ""+Math.round(thickness);
      if (thickness_str.length() < 2) thickness_str = "0" + thickness_str;
      return "NACA" + (Math.round(Math.abs(camber))) + "4" + thickness_str;
    }

    @Override
    String genReportText (double thickness, double camber) {
      return 
        "\n\n " + descr + getDescr(thickness, camber) + 
        "\n\n Tabulated Foil. Get geometry from this link:" +
        "\nhttp://airfoiltools.com/airfoil/naca4digit?MNaca4DigitForm%5Bcamber%5D=" + 
        filter3(camber) + 
        "&MNaca4DigitForm%5Bposition%5D=40&MNaca4DigitForm%5Bthick%5D=" + 
        filter3(thickness) + 
        "&MNaca4DigitForm%5BnumPoints%5D=81&MNaca4DigitForm%5BcosSpace%5D=0&MNaca4DigitForm%5BcosSpace%5D=1&MNaca4DigitForm%5BcloseTe%5D=0&yt0=Plot:";
    }
  } // class NACA4Foil
  
  class Tab15Foil extends Foil {
    // not yet double re;
    double thickness;
    double camber;
    double[] t_lift; // Cl
    double[] t_drag; // Cd
    double[] t_moment; // Cm
    String report_text;

    Tab15Foil (String descr, String print_line, String report_text, double thickness, double camber, double[] t_lift, double[] t_drag, double[] t_moment) {
      super(descr, print_line);
      this.report_text = report_text;
      this.thickness = thickness;
      this.camber = camber;
      this.t_lift = t_lift;
      this.t_drag = t_drag;
      this.t_moment = t_moment;
      check_arrays(size());
      reynolds_correction_fixpt = 300000;
    }

    int size () { return 15; }
    @Override
    boolean tabulated_coeffs () { return true; }

    void check_arrays (int sz) {
      if (t_lift.length != sz) 
        System.out.println("ERROR: foil " + descr + "has wrong length of Cl array: " + t_lift.length);
      if (t_drag.length != sz) 
        System.out.println("ERROR: foil " + descr + "has wrong length of Cd array: " + t_drag.length);
    }

    @Override
    double get_Cl (double effaoa) {
      return solver.ci15(t_lift, effaoa);
    }

    @Override
    double getCdragIdeal (double cldin, double effaoa, double thickness, double camber) {
      return solver.ci15(t_drag, effaoa);
    }

    @Override
    double getCmoment (double effaoa) {
      return t_moment == null ? 0 : solver.ci15(t_moment, effaoa);
    }

    @Override
    void adjust_foil_shape_in_tab () {
      current_part.thickness = thickness;
      current_part.camber = camber;
      // current_part.camber/25 = current_part.camber / 25.0;
      in.shp.set_camber_and_thickness_controls(false);
    }
    @Override
    String genReportText (double thickness, double camber) {
      return report_text;
    }
  }

  class Tab25Foil extends Tab15Foil {

    Tab25Foil (String descr, String print_line, String report_text, double thickness, double camber, double[] t_lift, double[] t_drag, double[] t_moment) {
      super(descr, print_line, report_text, thickness, camber, t_lift, t_drag, t_moment);
      reynolds_correction_fixpt = 300000;
    }

    @Override
    int size () { return 25; }

    @Override
    double get_Cl (double effaoa) {
      return solver.ci25(t_lift, effaoa);
    }

    @Override
    double getCdragIdeal (double cldin, double effaoa, double thickness, double camber) {
      return solver.ci25(t_drag, effaoa);
    }

    @Override
    double getCmoment (double effaoa) {
      return t_moment == null ? 0 : solver.ci25(t_moment, effaoa);
    }

  }

  Foil foil_arr[] = {
    new NACA4Foil("NACA 4 Series", "\n NACA 4 Series Foil"),
    // Tabulated Foils go here
    new Tab15Foil("Aquila 9.3%", "\n Aquila 9.3% Airfoil", 
                  "\n\n Tabulated Foil. Get geometry from this link:" +
                  "\n  http://airfoiltools.com/airfoil/details?airfoil=aquilasm-il",
                  9.3, 4, 
                  // Name = AQUILA 9.3% smoothed
                  // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
                  // Surface Finis    // h = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
                  //       Cl      //     Cd       Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
                  // [ ]   [-]     //     [-]      [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
                  // -28.0    -0.1    // 25   0.39381 -0.021  0.893   0.006   0.931   0.015   -0.317  0.278   0.082
                  // -24.0    -0.1    // 63   0.29085 -0.020  0.887   0.007   0.931   0.014   -0.561  0.272   0.128
                  // -20.0    -0.2    // 16   0.21866 -0.019  0.881   0.006   0.933   0.011   -0.989  0.259   0.162
                  // -16.0    -0.2    // 83   0.16381 -0.019  0.862   0.006   0.937   0.010   -1.727  0.251   0.183
                  // -12.0    -0.3    // 36   0.09896 -0.019  0.823   0.006   0.943   0.009   -3.398  0.241   0.194
                  // -8.0     -0.2    // 88   0.06640 -0.019  0.728   0.005   0.960   0.007   -4.342  0.253   0.184
                  // -4.0     -0.0    // 45   0.03735 -0.020  0.593   0.005   0.975   0.006   -1.216  0.310   -0.186
                  // -0.0     0.40    // 0    0.00847 -0.060  0.462   0.012   0.982   0.989   47.277  0.300   0.400
                  // 4.0      0.86    // 4    0.00661 -0.066  0.307   0.946   0.986   0.988   130.772 0.263   0.326
                  // 8.0      1.29    // 4    0.01596 -0.071  0.152   0.998   0.955   0.998   81.085  0.263   0.305
                  // 12.0     1.38    // 8    0.03889 -0.072  0.002   1.000   0.635   1.000   35.696  0.403   0.302
                  // 16.0     1.00    // 4    0.14560 -0.027  0.001   1.000   0.003   1.000   6.895   0.311   0.277
                  // 20.0     0.68    // 5    0.24856 -0.029  0.001   1.000   0.003   1.000   2.758   0.235   0.293
                  // 24.0     0.44    // 3    0.33959 -0.036  0.001   1.000   0.005   1.000   1.304   0.221   0.331
                  // 28.0     0.29    // 2    0.48325 -0.041  0.001   0.999   0.007   0.999   0.604   0.218   0.389
                  //                  // 
                  new double[]{-0.1 ,-0.1 ,-0.2 ,-0.2 ,-0.3 ,-0.2 ,-0.0 ,0.40 ,0.86 ,1.29 ,1.38 ,1.00 ,0.68 ,0.44 ,0.29 },
                  new double[]{0.39381, 0.29085, 0.21866, 0.16381, 0.09896, 0.06640, 0.03735, 0.00847, 0.00661, 0.01596, 0.03889, 0.14560, 0.24856, 0.33959, 0.48325 },
                  null),
    new Tab15Foil("NACA 63-412", "\n NACA 63-412 Foil",
                  "\n\n Tabulated Foil. Get geometry from this link:" +
                  "\n  http://airfoiltools.com/airfoil/details?airfoil=n63412-il",
                  12, 3.3, 
                  // Name = NACA 63-412 AIRFOIL
                  // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
                  // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
                  //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
                  // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
                  // -28.0    -0.246  0.48435 -0.015  0.945   0.002   1.000   0.011   -0.508  0.246   0.188
                  // -24.0    -0.341  0.38844 -0.016  0.930   0.002   1.000   0.008   -0.878  0.237   0.204
                  // -20.0    -0.474  0.25074 -0.018  0.920   0.003   1.000   0.009   -1.889  0.231   0.212
                  // -16.0    -0.614  0.14833 -0.021  0.900   0.004   1.000   0.010   -4.138  0.225   0.216
                  // -12.0    -0.641  0.08843 -0.022  0.845   0.007   1.000   0.008   -7.245  0.274   0.215
                  // -8.0     -0.425  0.05258 -0.025  0.751   0.008   1.000   0.009   -8.089  0.338   0.190
                  // -4.0     -0.087  0.01062 -0.071  0.647   0.009   1.000   0.931   -8.197  0.316   -0.567
                  // -0.0     0.386   0.00814 -0.079  0.547   0.517   1.000   0.954   47.401  0.266   0.455
                  // 4.0      0.856   0.01400 -0.087  0.006   0.660   1.000   0.954   61.186  0.173   0.351
                  // 8.0      0.981   0.05228 -0.033  0.004   0.705   0.005   0.955   18.765  0.029   0.284
                  // 12.0     1.087   0.08887 -0.036  0.005   0.838   0.005   0.957   12.228  0.935   0.283
                  // 16.0     0.989   0.15306 -0.038  0.004   0.888   0.006   0.951   6.461   0.232   0.289
                  // 20.0     0.708   0.22957 -0.043  0.004   0.922   0.007   0.951   3.085   0.232   0.310
                  // 24.0     0.480   0.34907 -0.048  0.003   0.930   0.009   0.956   1.375   0.226   0.350
                  // 28.0     0.329   0.46304 -0.052  0.002   0.930   0.011   0.956   0.710   0.223   0.408

                  new double[]{-0.246, -0.341, -0.474, -0.614, -0.641, -0.425, -0.087, 0.386 , 0.856 , 0.981 , 1.087 , 0.989 , 0.708 , 0.480 , 0.329     },
                  new double[]{0.48435, 0.38844, 0.25074, 0.14833, 0.08843, 0.05258, 0.01062, 0.00814, 0.01400, 0.05228, 0.08887, 0.15306, 0.22957, 0.34907, 0.46304  },
                  null),
    new Tab15Foil("Moth Bladerider V1", "\n Bladerider, modded NACA 63-412 with flap",
                  "\n\n Tabulated Foil." +
                  "\nAttempt to model Bladerider foil of International Moth Hydrofoiling Dinghy." +
                  "\nBladerider foil is modified NACA_63_412 with flap down." +
                  "\n  http://mothballone.blogspot.com/2013/07/bladerider-foil-mods.html" +
                  "\nGet geometry from Javafoil by:" +
                  "\n(1) obtaining geometry from " +
                  "\n  http://airfoiltools.com/airfoil/details?airfoil=n63412-il" +
                  "\n(2) inserting that into javafoil" +
                  "\n(3) mod it with trailing gap = 1, flaps: 3- 1, 25 1, 20 1.",
                  12, 4,
                  // attempt to model Bladerider foil. Bladerider foil is modified NACA_63_412 with flap down.
                  // the following is produced with (1) NACA_63_412 data from airfoils site inserted in javafoil
                  // (2) that foil is modded with trailing gap = 1, flaps: 3- 1, 25 1, 20 1.

                  // Name = NACA 63-412 AIRFOIL modded
                  // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
                  // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
                  //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
                  // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
                  // -28.0    -0.264  0.45319 -0.024  0.999   0.002   0.999   0.010   -0.582  0.231   0.160
                  // -24.0    -0.364  0.37355 -0.026  0.999   0.002   0.999   0.008   -0.974  0.224   0.179
                  // -20.0    -0.493  0.22506 -0.030  0.999   0.003   0.999   0.010   -2.191  0.219   0.190
                  // -16.0    -0.599  0.14758 -0.033  0.862   0.005   0.999   0.010   -4.055  0.135   0.195
                  // -12.0    -0.542  0.09361 -0.035  0.824   0.007   0.999   0.008   -5.788  0.270   0.185
                  // -8.0     -0.255  0.06026 -0.040  0.799   0.008   0.999   0.009   -4.232  0.358   0.094
                  // -4.0     0.148   0.01756 -0.110  0.751   0.416   0.999   0.956   8.449   0.340   0.991
                  // -0.0     0.625   0.01845 -0.119  0.568   0.520   0.999   0.958   33.893  0.269   0.440
                  // 4.0      1.086   0.02570 -0.128  0.006   0.650   0.999   0.958   42.265  0.108   0.368
                  // 8.0      1.124   0.06436 -0.048  0.004   0.677   0.005   0.959   17.464  -0.733  0.293
                  // 12.0     1.165   0.10404 -0.051  0.005   0.699   0.005   0.960   11.194  0.216   0.294
                  // 16.0     0.974   0.17417 -0.053  0.004   0.951   0.005   0.952   5.593   0.238   0.305
                  // 20.0     0.676   0.24795 -0.057  0.004   0.951   0.006   0.952   2.728   0.228   0.334
                  // 24.0     0.456   0.37413 -0.065  0.002   0.960   0.010   0.961   1.219   0.220   0.392
                  // 28.0     0.314   0.51586 -0.068  0.002   0.960   0.011   0.961   0.608   0.229   0.466
                  // 

                  new double[]{-0.264, -0.364, -0.493, -0.599, -0.542, -0.255, 0.148 , 0.625 , 1.086 , 1.124 , 1.165 , 0.974 , 0.676 , 0.456 , 0.314   },
                  new double[]{0.45319, 0.37355, 0.22506, 0.14758, 0.09361, 0.06026, 0.01756, 0.01845, 0.02570, 0.06436, 0.10404, 0.17417, 0.24795, 0.37413, 0.51586  },
                  null),
    new Tab15Foil("SD7084", "\n Selig/Donovan 9.6% low Reynolds number foil",
                  "\n\n Tabulated Foil. Get geometry from this link:" +
                  "\n  http://airfoiltools.com/airfoil/details?airfoil=sd7084-il",
                  9.6, 2.3,
                  // Name = SD7084 (9.6%)
                  // Mach = 0; Re = 700000; T.U. = 1.0; T.L. = 1.0
                  // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
                  // Alpha   Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
                  //         [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
                  // -28.0   -0.134  0.38042 -0.012  1.000   0.002   1.000   0.022   -0.353  0.253   0.161
                  // -24.0   -0.192  0.27887 -0.012  0.989   0.002   1.000   0.020   -0.690  0.251   0.189
                  // -20.0   -0.285  0.20817 -0.012  0.953   0.002   1.000   0.016   -1.370  0.251   0.209
                  // -16.0   -0.421  0.16472 -0.012  0.918   0.002   1.000   0.011   -2.555  0.252   0.223
                  // -12.0   -0.548  0.09257 -0.011  0.882   0.003   1.000   0.006   -5.916  0.215   0.229
                  // -8.0    -0.475  0.05259 -0.013  0.843   0.005   1.000   0.009   -9.030  0.330   0.222
                  // -4.0    -0.214  0.00829 -0.038  0.784   0.007   1.000   0.998   -25.880 0.290   0.072
                  // -0.0    0.251   0.00531 -0.043  0.581   0.766   1.000   0.998   47.285  0.260   0.420
                  // 4.0     0.717   0.00687 -0.047  0.238   0.972   0.992   0.991   104.263 0.261   0.316
                  // 8.0     1.111   0.01504 -0.052  0.007   0.981   0.940   0.992   73.861  0.261   0.297
                  // 12.0    1.068   0.03960 -0.051  0.001   0.988   0.517   0.992   26.978  0.316   0.298
                  // 16.0    0.706   0.13343 -0.025  0.001   0.989   0.009   0.993   5.293   0.288   0.286
                  // 20.0    0.452   0.20179 -0.028  0.001   0.990   0.010   0.994   2.238   0.235   0.311
                  // 24.0    0.286   0.29604 -0.031  0.001   0.991   0.012   0.994   0.966   0.216   0.360
                  // 28.0    0.189   0.38724 -0.037  0.001   0.991   0.018   0.996   0.488   0.197   0.444
                  new double[]{-0.134, -0.192, -0.285, -0.421, -0.548, -0.475, -0.214, 0.251, 0.717, 1.111, 1.068, 0.706, 0.452, 0.286, 0.189},
                  new double[]{0.38042, 0.27887, 0.20817, 0.16472, 0.09257, 0.05259, 0.00829, 0.00531, 0.00687, 0.01504, 0.03960, 0.13343, 0.20179, 0.29604, 0.38724},
                  null),
    new Tab25Foil("SD8040", "\n Selig/Donovan 10% low Reynolds number foil",
                  "\n\n Tabulated Foil. Get geometry from this link:" +
                  "\n http://airfoiltools.com/airfoil/details?airfoil=sd8040-il",
                  10, 2.1, 

   // Name = SD8040 (10%)
   // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
   // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
   // �    Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
   // [�]  [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
   // -24.0   -0.285  0.31478 -0.021  0.973   0.004   0.988   0.013   -0.904  0.259   0.178
   // -22.0   -0.329  0.26102 -0.020  0.969   0.004   0.988   0.012   -1.262  0.257   0.189
   // -20.0   -0.380  0.22511 -0.020  0.958   0.004   0.988   0.011   -1.690  0.259   0.198
   // -18.0   -0.435  0.18655 -0.019  0.945   0.003   0.988   0.009   -2.333  0.263   0.206
   // -16.0   -0.488  0.17173 -0.019  0.928   0.003   0.989   0.007   -2.845  0.262   0.212
   // -14.0   -0.530  0.13065 -0.018  0.909   0.003   0.989   0.006   -4.053  0.259   0.216
   // -12.0   -0.543  0.09601 -0.018  0.889   0.004   0.990   0.005   -5.651  0.282   0.217
   // -10.0   -0.510  0.07197 -0.019  0.864   0.004   0.990   0.006   -7.090  0.265   0.213
   // -8.0    -0.423  0.05525 -0.020  0.837   0.004   0.990   0.007   -7.648  0.275   0.203
   // -6.0    -0.285  0.04206 -0.024  0.806   0.005   0.991   0.018   -6.771  0.378   0.164
   // -4.0    -0.139  0.00857 -0.056  0.758   0.011   0.992   0.996   -16.265 0.337   -0.151
   // -2.0    0.094   0.00694 -0.058  0.678   0.435   0.993   0.997   13.546  0.257   0.862
   // -0.0    0.331   0.00658 -0.059  0.563   0.718   0.994   0.998   50.258  0.257   0.429
   // 2.0     0.565   0.00724 -0.061  0.418   0.882   0.992   0.997   78.092  0.258   0.358
   // 4.0     0.797   0.00897 -0.063  0.252   0.956   0.990   0.992   88.852  0.258   0.329
   // 6.0     1.016   0.01362 -0.065  0.120   0.981   0.972   0.992   74.627  0.258   0.314
   // 8.0     1.208   0.01785 -0.066  0.037   0.990   0.928   0.996   67.647  0.258   0.305
   // 10.0    1.347   0.02344 -0.067  0.014   0.994   0.851   0.997   57.455  0.247   0.300
   // 12.0    1.391   0.03665 -0.066  0.008   0.997   0.642   0.997   37.960  0.424   0.297
   // 14.0    1.246   0.09541 -0.050  0.005   0.997   0.136   0.997   13.063  0.361   0.290
   // 16.0    1.153   0.13607 -0.039  0.004   0.997   0.035   0.997   8.475   0.317   0.284
   // 18.0    1.040   0.17206 -0.036  0.003   0.997   0.019   0.997   6.043   0.263   0.285
   // 20.0    0.909   0.21938 -0.036  0.002   0.997   0.017   0.997   4.145   0.255   0.290
   // 22.0    0.778   0.27645 -0.035  0.001   0.997   0.012   0.998   2.815   0.258   0.295
   // 24.0    0.659   0.33062 -0.034  0.001   0.998   0.009   0.998   1.993   0.256   0.302

                  new double[]{-0.285, -0.329, -0.380, -0.435, -0.488, -0.530, -0.543, -0.510, -0.423, -0.285, -0.139, 0.094, 0.331, 0.565, 0.797, 1.016, 1.208, 1.347, 1.391, 1.246, 1.153, 1.040, 0.909, 0.778, 0.659},
                  new double[]{0.31478, 0.26102, 0.22511, 0.18655, 0.17173, 0.13065, 0.09601, 0.07197, 0.05525, 0.04206, 0.00857, 0.00694, 0.00658, 0.00724, 0.00897, 0.01362, 0.01785, 0.02344, 0.03665, 0.09541, 0.13607, 0.17206, 0.21938, 0.27645, 0.33062},
                  null),

    new Tab25Foil("NACA 64-814", "\n NACA 64-814 with trailing gap 0.5%",
                  "\n\n Tabulated Foil. Get geometry from this link:" +
                  "\n  http://www.airfoiltools.com/search/index?m%5Bgrp%5D=naca6&m%5Bsort%5D=1",
                  14, 5, 
                  // NACA 6 series variants
                  // use this to convert excel column of 25 values to array initializer:
                  // NACA 64_814 with TE gap = 0.5%
                  new double[]{-0.436, -0.495, -0.549, -0.585, -0.585, -0.521, -0.415, -0.271, -0.097, 0.112, 0.353, 0.596, 0.835, 1.073, 1.302, 1.494, 1.644, 1.715, 1.408, 1.315, 1.167, 1.003, 0.843, 0.702, 0.584},
                  new double[]{0.31324, 0.2687, 0.21301, 0.17075, 0.13549, 0.1072, 0.08288, 0.06413, 0.05002, 0.01114, 0.00998, 0.00805, 0.00833, 0.00811, 0.00777, 0.021, 0.02575, 0.03206, 0.08869, 0.11247, 0.14528, 0.18438, 0.23803, 0.29672, 0.34426},
                  null)
                  
    // FoilSim foils
    // these foils - Joukowski, Ellipse, Flat - 
    // work Ok but too slow for swipe-plots. See use_foilsim_foils
    //, new Foil("Joukowski","\n Joukowski Foil")
    //, new Foil("Ellipse", "\n Elliptical Foil")
    //, new Foil("Flat Plate", "\n Plate")
    // these come together with the Ball tab and not very useful for 
    // Hydrofoil sim by default. See flag use_cylinder_shapes
    // , new RoundFoil("Rotating Cylinder", "\n Rotating Cylinder")
    //, new RoundFoil("Spinning Ball","\n Spinning Ball")
  };

  Foil  FOIL_JOUKOWSKI     = (Foil)foils.get("Joukowski"),
    FOIL_ELLIPTICAL        = (Foil)foils.get("Ellipse"),
    FOIL_FLAT_PLATE        = (Foil)foils.get("Flat Plate"),
    FOIL_CYLINDER          = (Foil)foils.get("Rotating Cylinder"),
    FOIL_BALL              = (Foil)foils.get("Spinning Ball"),
    FOIL_NACA4             = (Foil)foils.get("NACA 4 Series");


  // need to grow foil_arr
  void addFoil(Foil foil) {
    Foil[] arr = new Foil[foil_arr.length+1];
    int ai = 0;
    for (; ai < foil_arr.length; ai++) arr[ai] = foil_arr[ai];
    arr[ai] = foil;
    foil_arr = arr;
  }
  
  static String dateString () {
    Date date = new Date();
    String date_str = "";
    /**
     * @j2sNative
     * 
     * var idx = date_str.indexOf("GMT");
     * if (idx>-1)date_str=date_str.substring(0,idx);
     *  
     */ {
      date_str = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(date);
    }
    return date_str;
  }

  static final int  IMPERIAL = 0, METRIC = 1, METRIC_2 = 2, NAVAL = 3, IMPERIAL_FEET = 4;
  static Font largeFont = new Font("TimesRoman", Font.PLAIN, 24);
 
  static double convdr = 3.1415926/180.;
  static double pid2 = 3.1415926/2.0;

  static double liftOverDrag,viscos;

  static double usq,vsq,alt,altmax;

  static double q0_EN,q0_SI,ps0,ts0,rho_EN,rho_SI,rlhum; // free stream vars

  final static double STEP_X = 0.4;
  static double xflow;             /* MODS  20 Jul 99 */
  static double delx,delt,velocity,spin,spindr,yoff,radius;
  static double vel,pres,side,omega,radcrv,relsy,angr;

  // 
  String t_foil_name, make_name, model_name, year_etc;
  // note on *speed*: here and below, it is velocity of the craft in
  // forvard direction.
  static String speed_kts_mph_kmh_ms_info = "";
  static String max_speed_info = "-", max_speed_cg = "-";
  double        max_speed_speed, max_speed_lift, max_speed_drag;
  static String min_takeoff_speed_info = "-", min_takeoff_cg = "-";
  double        min_takeoff_speed, min_takeoff_lift, min_takeoff_drag;
  static String cruising_info = "-", cruising_cg = "-";
  double        cruising_speed, cruising_lift, cruising_drag;
  static String constraint_lift_text_auw = "all-up weight (AUW)";
  static String constraint_lift_text_uplift = "required uplift force";

  // Foiling craft parts with default values
  Part //              name; airfoil     xpos             chord  span  thk camb aoa
  fuse    = new Part("Fuse", FOIL_NACA4, 0,               0.8,   0.02, 04, 0,   0),
    wing  = new Part("Wing", FOIL_NACA4, 0,               0.11,  0.56, 12, 3.34,0), 
    strut = new Part("Mast", FOIL_NACA4, fuse.chord/3,    0.111, 0.95, 12, 0,   0), 
    stab  = new Part("Stab", FOIL_NACA4, fuse.chord-0.05, 0.05,  0.3,  12, -3,  0)
    ;
  
  Part current_part = stab;  // anything goes here, avoid null

  static double BOARD_THICKNESS;
  static double BOARD_LENGTH;
  static double BOARD_WEIGHT;
  double board_drag = 0;

  static double RIG_WEIGHT;
  static double FOIL_WEIGHT = 20; // N, weight of typical semi-submerged foil (dry weight - floatation)
  static double MAST_LE_TO_TRANSOM = 0.3;
  static double WS_MASTBASE_MAST_LE = 1.04; // 1.04 is typical rig mast base from strut LE distance...
  static double mast_xpos; // mast xpos at board bottom's level!

  static double rider_weight;

  static double RIDER_DRIVE_HEIGHT = 1.05; // this is ~ where waist harness is

  // stall model coeffs cache
  // K = k0 + k1 *AoA + k2 *AoA*AoA +  k3 *AoA*AoA*AoA.
  static double stall_model_k0, stall_model_k1, stall_model_k2, stall_model_k3;
  double craft_pitch = 0;
  double mast_foot_pressure_k = 0.0;
  double mast_foot_dist_from_strut_le = 1;
  static Button part_button, bt_wing, bt_stab;

  // kitefoil or windfoil? 
  static final int KITEFOIL = 0, WINDFOIL = 1;
  int craft_type = WINDFOIL;

  static final Color color_sky_blue_light = new Color(102, 204, 255);
  static final Color color_water_dark = new Color(0, 136, 200);
  static final Color color_dark = new Color(90, 90, 90);
  static final Color color_very_dark = new Color(50, 50, 50);
  static final Color color_beige = new Color(245,245,220);
  static final Color color_light_cyan = new Color(224, 255, 255);

  String foil_descr (Foil foil) {
    return foil.getDescr(current_part.thickness, current_part.camber);
  }

  // final static int DRAG_COMP_POLY_FOILSIMORIG = 0,
  //       DRAG_COMP_AQUILA_9P3 = 1,
  //       DRAG_COMP_NACA4SERIES = 2;

  // wing
  static double wing_chord, wing_span, wing_area;

  static double[][] rg  = new double[20][40]; 
  static double[][] thg = new double[20][40]; 
  static double[][] xg  = new double[20][40]; 
  static double[][] yg  = new double[20][40]; 
  static double[][] xm  = new double[20][62]; 
  static double[][] ym  = new double[20][62]; 
  static double[][] xpl  = new double[20][62]; 
  static double[][] ypl  = new double[20][62]; 

  static double[] plp   = new double[40];
  static double[] plv   = new double[40];

  static double stall_model_apos = 10, stall_model_aneg = -10;
  
  static final boolean DEBUG_SPEED_SUPPR_ADJ = false;
  static final boolean DEBUG_SPEED_SUPPR     = false;
  static void debug_speed_suppr_adj (AdjustmentEvent e) {
    System.out.println("-- AdjustmentEvent"); }

  
  static final int STALL_MODEL_IDEAL_FLOW = 0,
    STALL_MODEL_DFLT = 1, // default 0.5 + 0.1A + 0.005A^2 and +-10
    STALL_MODEL_REFINED = 2; // this is per foil type.

  // wing downwash influence: makes effective stab AOA more negative
  static boolean stab_aoa_correction = false; 
  
  static final int POINTS_COUNT = 35;
  static final int POINTS_COUNT_HALF = POINTS_COUNT/2 + 1;
  static final int STREAMLINES_COUNT = 15;
  static final int STREAMLINES_COUNT_HALF = STREAMLINES_COUNT/2 + 1;
  static final int STREAMLINES_SKIP_BOTTOM = 4; // how many lines not to paint at the bottom

  int stall_model_type;

  int lunits,lftout,planet,dragOut;
  int display_units = METRIC;

  //  foil display state
  int displ; 

  static final int DISPLAY_STREAMLINES = 0,
    DISPLAY_ANIMATION = 1, 
    DISPLAY_DIRECTION = 2,
    DISPLAY_GEOMETRY = 3;

  static final int VIEW_EDGE = 0,
    VIEW_FORCES = 1,
    VIEW_3D_MESH = 2;

  int viewflg = VIEW_FORCES;


  static final int PLOT_TYPE_PRESSURE = 0, // pressure variation 
    PLOT_TYPE_VELOCITY = 1,  // velocity variation
    PLOT_TYPE_ANGLE = 2, // lift/cl/drag/cd versus angle
    PLOT_TYPE_THICKNESS = 3, // lift/cl/drag/cd versus thickness
    PLOT_TYPE_CAMBER = 4, // lift/cl/drag/cd versus camber
    PLOT_TYPE_CURR_PART_VS_SPEED = 5, // lift and drag versus speed
    PLOT_TYPE_ALTITUDE = 6,  // lift and drag versus altitude
    PLOT_TYPE_WING_AREA = 7, // lift and drag versus area
    PLOT_TYPE_DENSITY = 8,  // lift and drag versus density
    PLOT_TYPE_LIFT_DRAG_POLARS = 9,  // lift/drag polar
    PLOT_TYPE_CG_VS_SPEED = 10,  // cg position at various sppeds @ given load andA of min drag
    PLOT_TYPE_DRAG_TOTALS_VS_SPEED = 11,  // cg position at various sppeds @ given load andA of min drag

    PLOT_TYPE_GAGES = 20;

  int plot_type = PLOT_TYPE_LIFT_DRAG_POLARS;


  // applicable to PLOT_TYPE_ANGLE, PLOT_TYPE_THICKNESS, PLOT_TYPE_CAMBER
  static final int PLOT_OUT_LIFT = 0,
    PLOT_OUT_CL = 1,
    PLOT_OUT_DRAG = 2,
    PLOT_OUT_CD = 3, 
    PLOT_OUT_CM = 4;

  static final int PLOT_OUT_2_LIFT = 0,
    PLOT_OUT_2_DRAG = 1,
    PLOT_OUT_2_BOARD_DRAG = 2;

  int plot_y_val, plot_y_val_2;
    
  int zoom_slider_pos_y; 

  int bdragflag = 1;

  boolean  ar_lift_corr = true, re_corr = true, induced_drag_on = true;

  /* units data */
  static double v_min,alt_min,ang_min,v_max,alt_max,ang_max;
  static double ca_min,thk_min,ca_max,thk_max;
  static double chrd_min,span_min,ar_min,chrd_max,span_max,ar_max;
  static double rad_min,spin_min,rad_max,spin_max;
  static double vconv,vmax;
  static double pconv,pmax,pmin,lconv,rconv,fconv,fmax,fmaxb;
  static double load_min = 30, // Newtons
    load_max = 1500; // Newtons

  static double load = load_min;

  /*  probe data */
  static double prg,pthg,pxg,pyg,pxm,pym,pxpl,pypl;

  int xt1,yt1,xt2,yt2;
  static double fact,xpval,ypval,pbval;
  int pboflag,xt,yt;


  Solver solver = new Solver();
  VPP vpp       = new VPP();

  Controls con;
  In in;
  Out out;
  CardLayout layin,layout,layplt;
  Image offImg1;
  Graphics off1Gg;
  Image offImg2;
  Graphics off2Gg;
  Image offImg3;
  Graphics off3Gg;

  String getParameter (String name, String default_val) {
    String val = runAsApplication ? getProperty(name, default_val) : getParameter(name);
    if (val == null) val = default_val;
    return val;
  }

  boolean use_cylinder_shapes = false; // Cylinders not useful for Hydrofoil
  boolean use_foilsim_foils = false; // these foils make swipe plots too slow
  boolean foil_is_cylinder_or_ball (Foil foil) {
    return foil  == FOIL_CYLINDER || foil == FOIL_BALL;
  }

  /**
   * 
   * @j2sNative
   * 
   *  console.log(args);
   *  this.traceArray(args);
   */
  void trace (Object... args) {
    traceArray(args);
  }
  
 
  void traceArray (Object[] args) {
    for (int i = 0; i < args.length; ++i) {
      System.out.print("|" + args[i] + "| ");
    }
    System.out.println("");
  }

  boolean parseParameters (Part p, String p_name, String defaults) {
    String p_data   = getParamOrProp(p_name, defaults);
    if (p_data == null)
      return false;
    return parseParamData(p, p_name, p_data);
  }
      
  boolean parseParamData_debug = false;
  boolean parseParamData (Part p, String p_name, String p_data) {
    if (parseParamData_debug) trace("parseParamData, before:", p_name, p_data);
    String[] params = p_data.split("\\s+");
    int i = 0;
    
    String name = params[i++];
    p.foil = (Foil)foils.get(name);
    if (p.foil == null) { // convert _ to space
      p.foil = (Foil)foils.get(name.replace("_", " "));
    }
    if (p.foil == null) { 

      /**
       * @j2sNative
       * 
       *    var dummy = 0;
       */
      { // only in Java, not in JavaScript
        String path = getParamOrProp("__def_file_dirpath", "") + name;
        System.out.println("-- path: " + path);
        if (new File(path).exists()) {
          // create from file
          Import imp = new Import(path);
          imp.analyze();
          if (p == strut && imp.camber_pst != 0)
            imp.fixSymmetry("Mast"); // most import data leads to slight assymetric shape
          p.foil = new Tab25Foil(imp.getName(), "Imorted foil " + imp.getName(), "\n\n Geometry: \n" + imp.getGeometryAsText() + "\n\n Table: \n" + imp.descr,
                                 imp.thickness_pst, imp.camber_pst, 
                                 imp.Cl, imp.Cd,
                                 imp.Cm);

          p.foil.geometry = imp.getGeometry();
          p.foil.camber_line = imp.getCamberLine();
              
          int count  = imp.points_count();
          System.out.println("-- count: " + count);
          p.foil.points_x = new double[count];
          p.foil.points_y = new double[count];
          imp.getPoints(p.foil.points_x, p.foil.points_y);

          // need to ad p.foil to foil_arr
          addFoil(p.foil);
        }
      }

      if (p.foil == null) { 
        System.out.println("WARNING: foil name: " + name + " is not recognized, assigning NACA 4 Series...");
        p.foil = (Foil)foils.get("NACA 4 Series");
      }
    }

    // if (p_name.equals("Stab")) System.out.println("-- stab foil: " + p.foil);
    
    String[] chord_spec = params[i++].split("[:;]"); // possible chord value separators 
    p.chord_spec = chord_spec;

    // span value
    p.span = Double.parseDouble(params[i++]);

    // thickness value. optional, default is 12
    String thickness = params.length > i ? params[i++] : "12";
    int pst_pos = thickness.indexOf("%");
    if (pst_pos > -1)
      p.thickness = Double.parseDouble(thickness.substring(0, pst_pos));
    else {
      p.thickness = Double.parseDouble(thickness);
      if (p.thickness < 0.5) // user entered thickness in meters, convert to %
        p.thickness = 100* p.thickness / p.chord;
    }

    // camber value. optional, default is 0
    thickness = params.length > i ? params[i++] : "0";
    pst_pos = thickness.indexOf("%");
    if (pst_pos > -1)
      p.camber = Double.parseDouble(thickness.substring(0, pst_pos));
    else {
      p.camber = Double.parseDouble(thickness);
      if (Math.abs(p.camber) < 0.1) // user likely entererd camber in meters, convert to %
        p.camber = 100* p.camber / p.chord;
    }

    p.aoa = Double.parseDouble(params.length > i ? params[i++] : "0");

    // X axis position is LE position and is optional
    if (params.length > i)
      p.xpos = Double.parseDouble(params[i++]);
    else {  // recalc using actual fuse length 
      if (p == stab)
        p.xpos = fuse.chord - p.chord;
      else if (p == strut) 
        p.xpos = 0.3 * fuse.chord;
    }

    if (chord_spec.length == 1) { // simple rectangular shape
      p.chord = Double.parseDouble(chord_spec[0]);
      // *very* simple mesh
      if (p == strut) {
        p.mesh_LE = new Point3D[]{
          new Point3D(p.xpos, 0, 0),
          new Point3D(p.xpos, 0, p.span),
        };
        p.mesh_TE = new Point3D[]{
          new Point3D(p.xpos + p.chord, 0, 0),
          new Point3D(p.xpos + p.chord, 0, p.span),
        };
        p.chord_zoffs = p.span/2;
      } else { 
        double z_offset = 0;
        if (p == wing) z_offset = -0.04; // 4cm down 
        else if (p == stab) z_offset = 0.03; // 3 cm up
        p.chord_zoffs = z_offset;
        p.mesh_LE = new Point3D[]{
          new Point3D(p.xpos, -p.span/2, z_offset),
          new Point3D(p.xpos, p.span/2, z_offset),
        };
        p.mesh_TE = new Point3D[]{
          new Point3D(p.xpos + p.chord, -p.span/2, z_offset),
          new Point3D(p.xpos + p.chord,  p.span/2, z_offset),
        };
      }
    } else { // segments: chord[/xshift[/zshift]]:....
      double chords[] = new double[chord_spec.length];
      double xoffs[] = new double[chord_spec.length];
      double zoffs[] = new double[chord_spec.length];
      int specs_len = 0;
      double xoffs_prev = 0, zoffs_prev = 0;
      for (int ci = 0; ci < chord_spec.length; ci++) {
        String[] ch_x_z = chord_spec[ci].split("/");
        specs_len = Math.max(specs_len,ch_x_z.length);
        chords[ci] = Double.parseDouble(ch_x_z[0]);
        xoffs[ci] = xoffs_prev = xoffs_prev + 
          ((ch_x_z.length > 1) 
          ? Double.parseDouble(ch_x_z[1])
           : 0);
        zoffs[ci] = zoffs_prev = zoffs_prev + 
          ((ch_x_z.length > 2) 
          ? Double.parseDouble(ch_x_z[2])
           : 0);
        p.chord_zoffs+= zoffs_prev;
      }
      p.chord_zoffs /= chord_spec.length;
      if (parseParamData_debug) System.out.println("-- chord_zoffs: " + p.chord_zoffs);
      
      double xoff_tip  = xoffs_prev;
      // System.out.println("-- xoff_tip: " + xoff_tip);
      p.xoff_tip = xoff_tip;
      
      int segment_count = chord_spec.length -1;
      // compute MAC value. this is silly straightfoward
      // also approxinate MAC xpos... 
      // the idea is that mac_xpos*MAC*segment_count(segment_width = SUM each segement xpos * area...
      // which because width is the same for ech segement becomes
      // mac_xpos*MAC*segment_count = SUM each segement xpos * median chord
      double MAC = 0, segment_moments = 0;
      for (int ci = 0; ci < segment_count; ci++) {
        // effectibe chord of this segment
        double median_chord = (chords[ci] + chords[ci+1]) / 2;
        double median_xoff = (xoffs[ci] + xoffs[ci+1]) / 2;
        // effectibe chord of thsi segment added
        MAC += median_chord;
        segment_moments += median_xoff * median_chord;
      }
      MAC /= segment_count;
      p.chord = MAC;
      // now we derive mac xoffset!
      double mac_xoffs = segment_moments/(MAC*segment_count);
      if (parseParamData_debug) System.out.println("-- part: "+p_name+" MAC: " + MAC + " mac_xoffs: " + mac_xoffs);
      p.chord_xoffs = mac_xoffs;

      // estimate Ci_eff... how root chord, MAC and tip chord differ?
      // (- 1 (* 0.15 tip/mac))
      p.Ci_eff = 1 - 0.15 * (Math.min(1, chords[segment_count]/MAC));
      if (parseParamData_debug) System.out.println("-- Ci_eff: " + p.Ci_eff);
      // mesh. double segments for symmetric forms - wing, stab, fuse
      int mesh_count = (p == strut) ?  segment_count+1 : 2*segment_count+1;
      p.mesh_LE = new Point3D[mesh_count];
      p.mesh_TE = new Point3D[mesh_count];
      double z_offset = 0;
      if (specs_len < 3) {// no z coord detected
        if (p == wing) z_offset = -0.025; // down 
        else if (p == stab) z_offset = 0.02; // up
      }
      double segment_span = p.span/(mesh_count-1);
      if (p == strut) 
        for (int ci = 0; ci < mesh_count; ci++) {
          p.mesh_LE[ci] = new Point3D(p.xpos+xoffs[ci],           0, ci*segment_span);
          p.mesh_TE[ci] = new Point3D(p.xpos+chords[ci]+xoffs[ci],0, ci*segment_span);
        } else 
        for (int ci = 0; ci <= segment_count; ci++) {
          if (specs_len <  2) { // no xoffs, no z, align straight TE
            p.mesh_LE[segment_count + ci] = new Point3D(p.xpos+chords[0]-chords[ci],  ci*segment_span, z_offset);
            p.mesh_LE[segment_count - ci] = new Point3D(p.xpos+chords[0]-chords[ci], -ci*segment_span, z_offset);
            p.mesh_TE[segment_count + ci] = new Point3D(p.xpos+chords[0],             ci*segment_span, z_offset);
            p.mesh_TE[segment_count - ci] = new Point3D(p.xpos+chords[0],            -ci*segment_span, z_offset);
          } else { // no need to align alone x
            p.mesh_LE[segment_count + ci] = new Point3D(p.xpos+xoffs[ci],   ci*segment_span, z_offset+zoffs[ci]);
            p.mesh_LE[segment_count - ci] = new Point3D(p.xpos+xoffs[ci],   -ci*segment_span, z_offset+zoffs[ci]);
            p.mesh_TE[segment_count + ci] = new Point3D(p.xpos+chords[ci]+xoffs[ci],             ci*segment_span, z_offset+zoffs[ci]);
            p.mesh_TE[segment_count - ci] = new Point3D(p.xpos+chords[ci]+xoffs[ci],            -ci*segment_span, z_offset+zoffs[ci]);
          }
        }
      
    }

    p.compute_aux_params(); // area AR etc

    if (parseParamData_debug) trace("parseParamData, done:", p, p.foil.id, p_name, p_data);
        
    return true;
  }
  
  JDialog helpWindow;
  JDialog aboutWindow;
  
  void helpPopUp () {
    if (helpWindow == null) {
      JFrame frame = FoilBoard.frame != null ? FoilBoard.frame : new JFrame();
      //Panel panel = new Panel();
      String helpContent = "Help Help";
      JTextArea txtArea = new JTextArea(helpContent);
      txtArea.setEditable(false);
      txtArea.setFont(new Font("monospaced", Font.PLAIN, 12));
      txtArea.setBackground(Color.decode("#F0F0F0"));
      helpWindow = new JDialog(frame, "help", false);
      helpWindow.add(new JScrollPane(txtArea), BorderLayout.CENTER);
      helpWindow.setLocationRelativeTo(this);
      helpWindow.setLocation(100, 100);
      helpWindow.setSize(400, 700);
    }
    helpWindow.setVisible(true);
  }

  static String getProperty (String name, String dflt) {
    String val = props == null ? null : props.getProperty(name);
    if (val == null) val = System.getProperty(name, dflt);
    return val;
  }

  String getParamOrProp (String name, String dflt) {
    String val = runAsApplication ? null 
      : getParameter(name); // runAsApplication == false means this is applet
    if (val == null) val = getProperty(name, dflt);
    return val;
  }

  String getParamOrPropAliased (String name, String alias, String dflt) {
    String val = null; 
    if (!runAsApplication) { // runAsApplication == false means this is applet
      val = getParameter(name); 
      if (val == null) val = getParameter(alias);
    }
    if (val == null) val = getProperty(name, null);
    if (val == null) val = getProperty(name, dflt);
    return val;
  }

  @Override
  public void init () {
    //setPreferredSize(new Dimension(1000, 800));
    setSize(900, 600);

    // any inputs?
    //
    // note: defaults are typical windfoiler's, see kitefoil.sh for
    // typical kiter's values note 2: some of these were originally
    // constants hence uppercased, but then became initialized from
    // props...
    craft_type      = getParamOrPropAliased("TYPE","CRAFT_TYPE","WIND").toUpperCase().startsWith("WIND") ? WINDFOIL : KITEFOIL;
    BOARD_THICKNESS = Double.parseDouble(getParamOrPropAliased("BH",  "BOARD_THICKNESS", craft_type == WINDFOIL ? "0.1" : "0.06"));
    BOARD_LENGTH    = Double.parseDouble(getParamOrPropAliased("BL",  "BOARD_LENGTH", craft_type == WINDFOIL ? "2.3" : "1.25"));
    // Board weight force in Newtons
    BOARD_WEIGHT    = Double.parseDouble(getParamOrPropAliased("BW",  "BOARD_WEIGHT", craft_type == WINDFOIL ? "80" : "39"));
    RIG_WEIGHT      = Double.parseDouble(getParamOrPropAliased("RW",  "RIG_WEIGHT", craft_type == WINDFOIL ? "130" : "0"));
    rider_weight    = Double.parseDouble(getParamOrPropAliased("RSL", "TOTAL_WEIGHT", "735")) -
      BOARD_WEIGHT - RIG_WEIGHT - FOIL_WEIGHT;

    use_cylinder_shapes = Boolean.parseBoolean(getParamOrPropAliased("CYLS", "USE_CYLINDER_SHAPES", "false"));
    use_foilsim_foils   = Boolean.parseBoolean(getParamOrPropAliased("FSIMFOILS", "USE_FOILSIM_FOILS", "false"));


    // Parse parameters. Parse Fuse first so that default xpos for 
    // other foisl parts can be computed, then the rest of them.
    parseParameters(fuse, "Fuse", 
                    craft_type == WINDFOIL 
                    ? "NACA_4_Series 0.80 0.02 4 0 -1"
                    : "NACA_4_Series 0.55 0.02 4 0 -1");
    parseParameters(wing, "Wing", craft_type == WINDFOIL
                    ? "NACA_4_Series 0.170;0.160;0.150;0.135;0.110;0.045 0.75 12 3.34 0"
                    : "NACA_4_Series 0.09 0.54 12 3.34 0");
    parseParameters(stab, "Stab", craft_type == WINDFOIL
                    ? "NACA_4_Series 0.07 0.45 12 -3 0"
                    : "NACA_4_Series 0.061 0.315 12 -3.34 0");
    if (getParamOrProp("Strut", null) != null)
      parseParameters(strut, "Strut", null);
    else 
      parseParameters(strut, "Mast", craft_type == WINDFOIL 
                      ? "NACA_4_Series 0.125 0.85 12 0 0 0.33"
                      : "NACA_4_Series 0.12 1.04 12 0 0");
    
    mast_xpos = strut.xpos + strut.xoff_tip; // mast xpos at board bottom's level!

    make_name  = getParameter("Make", "Make:N/A").trim();
    model_name = getParameter("Model", "Model:Test").trim();
    year_etc   = getParameter("Year/Variant", null);
    if (year_etc == null)  year_etc = getParameter("Year", "V1");
    year_etc.trim();
    t_foil_name = (make_name + " " + model_name + " " + year_etc).trim();

    // Meed to show FoilSim foils?
    if (use_foilsim_foils) {
      addFoil(new Foil("Joukowski","\n Joukowski Foil"));
      addFoil(new Foil("Ellipse", "\n Elliptical Foil"));
      addFoil(new Foil("Flat Plate", "\n Plate"));
    }

    // adjust pre-loaded foils
    if (use_cylinder_shapes) {
      addFoil(new RoundFoil("Rotating Cylinder", "\n Rotating Cylinder"));
      addFoil(new RoundFoil("Spinning Ball","\n Spinning Ball"));
    }

    offImg1 = createImage(this.size().width,
                          this.size().height);
    off1Gg = offImg1.getGraphics();
    offImg2 = createImage(this.size().width,
                          this.size().height);
    off2Gg = offImg2.getGraphics();
    offImg3 = createImage(this.size().width,
                          this.size().height);
    off3Gg = offImg3.getGraphics();

    setLayout(new GridLayout(2,2,5,5));

    con = new Controls(this);

    // prelim
    lunits = METRIC;
    setUnits();

    solver.setDefaults();

    in = new In(this);
    out = new Out(this);

    add(out.viewer);
    add(con);
    add(in);
    add(out);

    solver.getFreeStream ();
    computeFlow();
    out.viewer.start();
    out.plot.start();

    // now, simulate switch to underfater, ocean water conditions
    // with metric units
    
    planet = 2; // water
    solver.getFreeStream ();
    // in.env.rightPanel.plntch.setSelectedIndex(planet);

    lunits = METRIC;
    setUnits();
    con.untch.setSelectedIndex(METRIC);

    velocity = 20.0; // speed is 20 km/h
    alt = 70; // depth: 30% of strut
    
    // Stab in downwash? if so, turn correction 'on' by default
    if (wing.xpos + wing.chord_xoffs < stab.xpos + stab.chord_xoffs ) {
      stab_aoa_correction = true;
      in.anl.chb_stab_aoa_corr.setSelected(true);
    }
    
    computeFlowAndRegenPlotAndAdjust();
    // make flight tab active
    //con.ibt_flight_al.actionPerformed(new ActionEvent((Object)con.ibt_flight, ActionEvent.ACTION_PERFORMED, ""));
    // temporary switch to stab
    //con.bt_stab_al.actionPerformed(new ActionEvent((Object)bt_stab, ActionEvent.ACTION_PERFORMED, ""));
    // make wing part active
    //con.bt_wing_al.actionPerformed(new ActionEvent((Object)bt_wing, ActionEvent.ACTION_PERFORMED, ""));

    inited = true;

    // System.out.println("-- stab foil: " + stab.foil);

    con.switch_to_part(wing);
    con.recomp_all_parts();
    computeFlowAndRegenPlotAndAdjust();
     loadOutPanel();
    vpp.steady_flight_at_given_speed(5, 0);
    out.plot.loadPlot();

    //debug System.out.println("-- FOIL_JOUKOWSKI: " + FOIL_JOUKOWSKI);
    //debug System.out.println("-- FOIL_ELLIPTICAL: " + FOIL_ELLIPTICAL);
    //debug System.out.println("-- FOIL_FLAT_PLATE: " + FOIL_FLAT_PLATE);
    //debug System.out.println("-- FOIL_CYLINDER: " + FOIL_CYLINDER);
    //debug System.out.println("-- FOIL_BALL: " + FOIL_BALL);
    //debug System.out.println("-- FOIL_NACA4: " + FOIL_NACA4);

    //debug { // profiling-aiding switches
    //debug   plot_type = PLOT_TYPE_CG_VS_SPEED;
    //debug   plot_y_val = 0;
    //debug   out.setSelectedIndex(0);
    //debug   out.plot.loadPlot();
    //debug   in.setSelectedIndex(1);
    //debug }

    if (getParamOrProp("LIST_FOILS", null) != null)
      for(Object key: foils.keySet()) System.out.println(("    "+key).replace(" ", "_"));
  }
 
  public Insets insets () {
    return new Insets(10,10,10,10);
  }

  double effective_aoa () {
    // strut is vertical and pitch does nto affect it and vice versa.
    // hence for strut, effective_aoa is its own aoa.
    if (current_part == strut)
      return current_part.aoa;
    else if (// false && // DEBUG aft problems...
             current_part == stab && stab_aoa_correction) {
      // stab is in downwash from main wing and sees some down stream.
      double wing_Cl0 = 0.4; // very typical, true for Aquila, NACA 3412 63-412 etc
      // the following is from "General aviation aicraft design, Appendix C1"
      double wing_Cl = wing.cl;
      double div = Math.PI * wing.span/wing.chord;
      double corrected_aoa = craft_pitch * (1 - 2 * wing_Cl / div) - 
        2 * wing_Cl0 / div - wing.aoa + current_part.aoa;
      // double correction = corrected_aoa - (current_part.aoa + craft_pitch);
      // System.out.println("-- correction: " + correction);
      return Math.min(ang_max, Math.max(ang_min, corrected_aoa));
    } else
      return Math.min(ang_max, Math.max(ang_min, current_part.aoa + craft_pitch));
  }

  // this avoids some overhead when doing massive computation
  boolean can_do_gui_updates = true;

  public void computeFlow () { 
    double effaoa = effective_aoa();
    if (!can_do_gui_updates && current_part.foil.tabulated_coeffs()) {
      solver.set_q0();
      current_part.cl = solver.get_Cl(effaoa);
    } else {
      solver.getFreeStream();
      solver.getCirculation(effaoa, current_part.thickness, current_part.camber);
      solver.compute_foil_geometry(effaoa);
      current_part.cl = solver.get_Cl(effaoa);
      solver.genFlow(effaoa);
    } 
    
    current_part.cm = current_part.foil.getCmoment(effaoa);
    // AR correction, per javafoil user guide etc
    current_part.cm *= current_part.aspect_rat/(current_part.aspect_rat+4);

    // test: is this the same value getCl_plot gives?
    // if (false) {
    //   double getCl_plot_val = out.plot.getCl_plot(current_part.camber/25, current_part.thickness/25, effaoa);
    //   if (getCl_plot_val != current_part.cl) {
    //     System.out.println("-- getCl_plot_val: " + getCl_plot_val + "yet current_part.cl: " + current_part.cl);
    //   }
    //   track_current_part.cl_changes();
    // }

    current_part.reynolds = foil_is_cylinder_or_ball(current_part.foil) 
      ? velocity/vconv * 2 * radius/lconv * rho_EN / viscos
      : velocity/vconv * current_part.chord/lconv * rho_EN / viscos;

    if (can_do_gui_updates)
      solver.getProbe();

    double thkd = current_part.thickness;
    double camd = current_part.camber;

    double alfd = effaoa; // effective_aoa();
    //   attempt to fix symmetry problem
    if (camd < 0.0) alfd = - alfd;
    
    // this saved current_part's cd, cd_profile and cd_aux
    solver.get_Cd(current_part.cl, alfd, thkd, camd); 
 
  }

  void computeFlowAndRegenPlot () {
    computeFlow();
    loadOutPanel();
    if (can_do_gui_updates) {
      out.plot.loadPlot();
    }
  }

  double limit (double min, double val, double max) {
    if (val < min) return min;
    if (val > max) return max;
    return val;
  }

  String pprint (int val) { return String.valueOf(val); }
  String pprint (float val) {
    int f0 = filter0(val);
    if ((float)f0 == val)
      return String.valueOf(f0);
    else
      return String.valueOf(val);
  }

  public int filter0 (double inumbr) {
    return (int) (Math.round(inumbr));
  }

  public float filter1 (double inumbr) {
    //  output only to .1
    float number;
    int intermed;
 
    intermed = (int) (inumbr * 10.);
    number = (float) (intermed / 10. );
    return number;
  }
 
  public float filter3 (double inumbr) {
    //  output only to .001
    float number;
    int intermed;
 
    intermed = (int) (inumbr * 1000.);
    number = (float) (intermed / 1000. );
    return number;
  }
 
  public float filter5 (double inumbr) {
    //  output only to .00001
    float number;
    int intermed;
 
    intermed = (int) (inumbr * 100000.);
    number = (float) (intermed / 100000. );
    return number;
  }
 
  public float filter9 (double inumbr) {
    //  output only to .000000001
    float number;
    int intermed;
 
    intermed = (int) (inumbr * 1000000000.);
    number = (float) (intermed / 1000000000. );
    return number;
  }
 
  public void setUnits () {   // Switching Units
    double ovs,chords,spans,aros,chos,spos,rads;
    double alts,ares;

    alts = alt / lconv;
    //chords = chord / lconv;
    // spans = span / lconv;
    //ares = area /lconv/lconv;
    //aros = arold /lconv/lconv;
    // chos = chrdold / lconv;
    //spos = span_old / lconv;
    ovs = velocity / vconv;
    rads = radius / lconv;

    switch (lunits) {
    case 0: {                             /* English: feet seconds lbs */
      lconv = 1.;                      /*  feet    */
      vconv = .6818; vmax = 250.;   /*  1 fps to 1 mph  */
      if (planet == 2) vmax = 50.;
      fconv = 1.0; fmax = 100000.; fmaxb = .5;  /* pounds   */
      pconv = 14.7;                   /* lb/sq in */
      break;
    }
    case 1: {                             /* Metric */
      lconv = .3048; /*  1 ft to 1 m */
      vconv = 1.097;  /* ?? fps to kmh?  */
      vmax = 400.;  
      if (planet == 2) vmax = 80.;
      fconv = 4.448; fmax = 500000.; fmaxb = 2.5; /* newtons */
      pconv = 101.3;               /* kilo-pascals */
      break;
    }
    }
 
    alt = alts * lconv;
    //chord = chords * lconv;
    //span = spans * lconv;
    // area = ares * lconv * lconv;
    //arold = aros * lconv * lconv;
    // chrdold = chos * lconv;
    //span_old = spos * lconv;
    velocity  = ovs * vconv;
    radius  = rads * lconv;
  }

  // convert speed in kts to current units
  double kts_to_speed (double kts) {
    return (lunits == METRIC) 
      ? kts / 0.539957
      : kts / 0.868976;
  }

  String make_speed_kts_mph_kmh_ms_info (double speed) {
    return (lunits == IMPERIAL)
      ? (filter1(speed * 0.868976) + " kt or " + filter1(speed) + " mph or " + 
         filter1(speed * 1.60934) + " km/h or " + filter1(speed * 0.44704) + " m/s")
      : (filter1(speed * 0.539957) + " kt or " + filter1(speed * 0.621371) + " mph or " + 
         filter1(speed)  + " km/h or " + filter1(speed * 0.277778) + " m/s");
  }

  float filter1or3 (double val) {
    double abs = Math.abs(val);
    if (val < 1) return filter3(val);
    else return filter1(val);
  }


  double speed_kmh_to_display_units (double speed_kmh) {
    switch (display_units) {
    case IMPERIAL: return speed_kmh *= 0.621371;
    default:
    case METRIC  : return speed_kmh *= 0.277778;
    case METRIC_2: return speed_kmh;
    case NAVAL:    return speed_kmh *= 0.539957;
    }
  }
  String make_speed_info_in_display_units (double speed, boolean unit_suffix) {
    String suffix = "";
    switch (display_units) {
    case IMPERIAL: speed *= 0.621371; suffix = " mph"; break;
    case METRIC  : speed *= 0.277778; suffix = " m/s"; break;
    case METRIC_2: suffix = " km/h"; break;
    case NAVAL:    speed *= 0.539957; suffix = " kt"; break;
    }
    if (unit_suffix)
      return pprint(filter1or3(speed)) + suffix;
    else 
      return pprint(filter1or3(speed));
  }

  String current_display_speed_unit_string () {
    switch (display_units) {
    case IMPERIAL: return "mph";
    default:
    case METRIC  : return "m/s";
    case METRIC_2: return "km/h"; 
    case NAVAL:    return "kt";
    }
  }

  double speed_input_in_display_units_to_kmh (JTextField f, int units) {
    return speed_input_in_display_units_to_kmh(Double.valueOf(f.getText()).doubleValue(), units);
  }
  double speed_input_in_display_units_to_kmh (double speed, int units) {
    switch (units) {
    case IMPERIAL: return speed * 1.60934;
    case METRIC  : return speed * 3.6;
    case NAVAL:    return speed * 1.852; 
    default:
    case METRIC_2: return speed;
    }
  }
      
  double size_input_in_display_units_to_m (double size, int units) {
    switch (units) {
    case NAVAL:
    case IMPERIAL: return size * 0.0254;
    case IMPERIAL_FEET: return size * 0.3048;
    case METRIC_2: return size * 0.01;
    default:
    case METRIC  : return size;
    }
  }
  double size_input_in_display_units_to_m (JTextField f, int units) {
    return size_input_in_display_units_to_m(Double.valueOf(f.getText()).doubleValue(), units);
  }

  double force_input_in_display_units_to_n (double val, int units) {
    switch (units) {
    case NAVAL:
    case IMPERIAL: 
    case IMPERIAL_FEET: return val * 4.448222;
    case METRIC_2: return val * 9.80665003;
    default:
    case METRIC  : return val;
    }
  }
  double force_input_in_display_units_to_n (JTextField f, int units) {
    return force_input_in_display_units_to_n(Double.valueOf(f.getText()).doubleValue(), units);
  }


  String make_size_info_in_display_units (double size, boolean unit_suffix) {
    String suffix = "";
    switch (display_units) {
    case NAVAL:
    case IMPERIAL: size *= 12*3.28084; suffix = " in"; break;
    case IMPERIAL_FEET: size *= 3.28084; suffix = " ft"; break;
    case METRIC  : suffix = " m"; break;
    case METRIC_2: size *= 100; suffix = " cm"; break;
    }
    if (unit_suffix)
      return pprint(filter1or3(size)) + suffix;
    else 
      return pprint(filter1or3(size));
  }

  String current_display_size_unit_string () {
    switch (display_units) {
    case NAVAL:
    case IMPERIAL: return "in"; 
    case IMPERIAL_FEET: return "ft"; 
    default:
    case METRIC  : return "m";
    case METRIC_2: return "cm"; 
    }
  }
      
  String make_area_info_in_display_units (double area, boolean unit_suffix) {
    String suffix = "";
    switch (display_units) {
    case NAVAL:
    case IMPERIAL: area *= 1550; suffix = " sq in"; break;
    case IMPERIAL_FEET: area *= 10.7639; suffix = " sq ft"; break;
    case METRIC  : suffix = " sq m"; break;
    case METRIC_2: area *= 10000; suffix = " sq cm"; break;
    }
    if (unit_suffix)
      return pprint(filter1or3(area)) + suffix;
    else 
      return pprint(filter1or3(area));
  }

  double force_n_to_display_units (double force_n) {
    switch (display_units) {
    case NAVAL: 
    case IMPERIAL: return force_n *= 0.224808942443;
    default:
    case METRIC  : return force_n;
    case METRIC_2: return force_n *= 0.101971621; 
    }
  }
      

  String make_force_info_in_display_units (double force, boolean unit_suffix) {
    String suffix = "";
    switch (display_units) {
    case NAVAL: 
    case IMPERIAL: force *= 0.224808942443; suffix = " Lbs"; break;
    case METRIC  : suffix = " N"; break;
    case METRIC_2: force *= 0.101971621; suffix = " kg"; break;
    }
    if (unit_suffix)
      return pprint(filter1or3(force)) + suffix;
    else 
      return pprint(filter1or3(force));
  }
      
  String current_display_force_unit_string () {
    switch (display_units) {
    case NAVAL: 
    case IMPERIAL: return "Lbs"; 
    default:
    case METRIC  : return "N"; 
    case METRIC_2: return "kg"; 
    }
  }
      
  String make_force_newtons_lbs_kg_info (double force, int sz) {
    return (lunits == IMPERIAL)
      ? (filter1(force * 4.44822) + " Newtons or " + filter1(force) + " Lbs or " + 
         filter1(force * 0.453592) + " kg")
      : (filter1(force) + " Newtons or " + 
         filter1(force * 0.224808942443)  + " Lbs or " + filter1(force * 0.101971621) + " kg");
  }

  String make_power_info_in_display_units (double force, double speed, boolean unit_suffix) {
    String suffix = "";
    double res = 0;
    switch (display_units) {
    case NAVAL:   
    case IMPERIAL: res = force * 0.00134102 * speed * 0.277778; suffix = " HP"; break;
    default:
    case METRIC:
    case METRIC_2: res = force * speed * 0.277778; suffix = " W"; break;
    }
    if (unit_suffix)
      return pprint(filter1or3(res)) + suffix;
    else 
      return pprint(filter1or3(res));
  }
      


  static String padRight (String s, int n) {
    return String.format("%1$-" + n + "s", s);  
  }

  static String padLeft (String s, int n) {
    return String.format("%1$" + n + "s", s);  
  }

  String maybe_warn_about_excessive_cg_offset () {
    if (con.cg_pos_board_level > 1.50) // oops, way too much forward!
      return "\nWarning: required rider stance indicates wrong setup;\nlikely, stab's negative lift is excessive.";
    else if (con.cg_pos_board_level > 0.60) // auch, forward of front strap, awkward!
      return "\nWarning: required rider stance indicates\nthat stab's negative lift likely needs be reduced.";
    else if (con.cg_pos_board_level < -0.20) // required stance (or "position on the board") is way to much aft
      return "\nWarning: rider's stance excessively aft the mast\nindicates that comfortable riding with required\npull force needs increase of stab's downward angle.";
    else 
      return "";
  }

  String make_min_takeoff_speed_info(double min_lift, double max_drag, double speed) {
    min_takeoff_speed = speed;
    in.flt.tf_cruise_starting_speed.setText(""+filter0(min_takeoff_speed));
    min_takeoff_lift  = min_lift;
    min_takeoff_drag  = max_drag;
    min_takeoff_cg =  con.outCGPosition.getText();
    return min_takeoff_speed_info = 
      "This foil has been evaluated for minimum possible \n" + 
      "takeoff speed.\n" +
      "It was found that with " + constraint_lift_text_uplift + " of \n" + 
      make_force_newtons_lbs_kg_info(filter1(min_lift),10) + " \n" + 
      "and total hydrodynamic drag not exceeding \n" + 
      make_force_newtons_lbs_kg_info(filter1(max_drag),10) + " \n" + 
      "it can take off at speed of foiling as low as \n" + 
      //filter1(velocity) + " km/h"
      make_speed_kts_mph_kmh_ms_info(speed) + 
      "\nLift is located " + min_takeoff_cg + " mast." +
      maybe_warn_about_excessive_cg_offset()
      ;
  }

  String make_cruising_info(double min_lift, double min_drag, double speed) {
    cruising_speed = speed;
    cruising_lift  = min_lift;
    cruising_drag  = min_drag;
    cruising_cg =  con.outCGPosition.getText();
    return cruising_info = 
      "This foil has been evaluated for minimum possible \n" + 
      "drag during cruising at speeds >= takeoff speed.\n" +
      "It was found that with " + constraint_lift_text_uplift + " of \n" + 
      make_force_newtons_lbs_kg_info(filter1(min_lift),10) + " \n" + 
      "total hydrodynamic drag would be minimal\nand not exceeding \n" + 
      make_force_newtons_lbs_kg_info(filter1(min_drag),10) + " \n" + 
      "L/D ratio = " + filter1(min_lift/min_drag) + ", when cruise-foiling at \n" + 
      //filter1(velocity) + " km/h"
      make_speed_kts_mph_kmh_ms_info(speed) + 
      "\nLift is located " + cruising_cg + " mast." +
      maybe_warn_about_excessive_cg_offset()
      ;
  }

  String make_max_speed_info(double min_lift, double max_drag, double speed) {
    max_speed_speed = speed;
    max_speed_lift  = min_lift;
    max_speed_drag  = max_drag;
    max_speed_cg = con.outCGPosition.getText();
    return max_speed_info = 
      "This foil has been evaluated for maximum possible \n" 
      + "sustained, controllable flight speed.\n" +
      "It was found that with " + constraint_lift_text_uplift + " of \n" + 
      make_force_newtons_lbs_kg_info(filter1(min_lift),10) + " \n" + 
      "and total hydrodynamic drag not exceeding \n" + 
      make_force_newtons_lbs_kg_info(filter1(max_drag),10) + " \n" + 
      "it can maintain sustained flight speed of at least \n" + 
      //filter1(velocity) + " km/h"
      make_speed_kts_mph_kmh_ms_info(speed) + 
      "\nLift is located " + max_speed_cg + " mast." +
      maybe_warn_about_excessive_cg_offset()
      ;
  }

  // used for speed....
  String toStringOptQMFilter1 (double val) {
    return (val <= 0) ? "??" : make_speed_info_in_display_units(val, false);
  }
  // used for force....
  String toStringOptQMFilter0 (double val) {
    return (val <= 0) ? "??" : make_force_info_in_display_units(val, false);
  }

  // loadInput has been removed.
  // Oct 17: fed up with loadInput, try not to call this anymore....
  // note: this also does computeFlow(), 
  // hence no need to do {computeFlowAndRegenPlotAndAdjust();computeFlowAndRegenPlot();}
  // public void loadInput () {   // load the input panels
  //   ... lots of code
  // }

  public void computeFlowAndRegenPlotAndAdjust () { 
    computeFlowAndRegenPlot();
    if (can_do_gui_updates)
      out.viewer.find_it();
    return;
  }

  void updateTotals () {

    // System.out.println("-- updateTotals... " + can_do_gui_updates);
    // new Exception("-- updateTotals  ---------------- ").printStackTrace(System.out);

    speed_kts_mph_kmh_ms_info = make_speed_kts_mph_kmh_ms_info(velocity);

    double lift = total_lift();
    double drag = total_drag();

    // Mtipping is 0.5*eff_strut_span*strut,drag
    double cg_position = find_cg_xpos(lift, // lift equals load
                                      drag, // drga equals driving force 
                                      // how much above fuse is driving force?
                                      strut.span + BOARD_THICKNESS + RIDER_DRIVE_HEIGHT +
                                      (craft_type == WINDFOIL ? 1 : 0),
                                      -(  // drag moment is above wings and is CW, hence negative
                                        // and is strut drug 
                                        strut.drag * 
                                        // multiplied by the arm
                                        (1-alt/100) * strut.span * // typically 30% of strut in the water
                                        0.5 *  +
                                        // should include aerodynamic drag here???
                                        0 // actually, no. It is so because a portion of sail's forward drive compensates it.
                                          ),
                                      // board moment plus rig moement
                                      (BOARD_WEIGHT*
                                       (BOARD_LENGTH/2-MAST_LE_TO_TRANSOM) // board weight arm is 1/2 length minus mast LE to transom
                                       + RIG_WEIGHT*WS_MASTBASE_MAST_LE) // 
                                      );
    // negative means aft MAST LE
    //System.out.println("-- cg_position: " + cg_position);
    con.cg_pos = cg_position;
    // correct cg_position for board-level mast LE x offset, if any.
    // note that positive xoff_tip reduces aft cg_position because curretly/historically cg_position
    // ofsset is negative in positive x direction. 
    cg_position += strut.xoff_tip;
    con.cg_pos_board_level = cg_position;

    if (can_do_gui_updates) {
      // new Exception("-- updateTotals in GUI  ---------------- ").printStackTrace(System.out);

      con.outTotalLift.setForeground(Color.yellow);
      con.outTotalLift.setText(make_force_info_in_display_units(lift, true));
      con.outTotalDrag.setForeground(Color.yellow);
      con.outTotalDrag.setText(make_force_info_in_display_units(drag, true));
      con.outPower.setText(make_power_info_in_display_units(drag, velocity, true));
      con.outTotalLDRatio.setText(""+filter1(lift/drag));

      if (cg_position >= 0.01) { // 1cm or more fore
        // con.lbl_cg_pos.setText("CG to Mast");
        if (display_units == METRIC || display_units == METRIC_2)
          con.outCGPosition.setText(pprint(filter0(    100*Math.abs(cg_position/*-wing.xpos*/))) + " cm fore");
        else
          con.outCGPosition.setText(pprint(filter0(39.3701*Math.abs(cg_position/*-wing.xpos*/))) + " in fore");
      } else if (cg_position > - 0.01) { // above mast LE
        // con.lbl_cg_pos.setText("CG above Mast");
        con.outCGPosition.setText("Above");
        // con.outCGPosition.setText("" + filter0(100*cg_position/fuse.chord) + " % aft");
      } else {
        // con.lbl_cg_pos.setText("CG to Mast");
        if (display_units == METRIC || display_units == METRIC_2)
          con.outCGPosition.setText(pprint(filter0(    100*Math.abs(cg_position/*-wing.xpos*/))) + " cm aft");
        else
          con.outCGPosition.setText(pprint(filter0(39.3701*Math.abs(cg_position/*-wing.xpos+strut.chord*/))) + " in aft");
      }
    }
  }

  // static void track_current_part.cl_changes () {
  //   current_part.cl_set_count++;
  //   if (current_part.cl != current_part.cl_old) {
  //     System.out.println("-- current_part.cl: " + current_part.cl + " set: " + current_part.cl_set_count);
  //     current_part.cl_old = current_part.cl;
  //   }
  // }

  // output routine
  // currently it has two disinct parts:
  // 1. compute forces
  // 2. load dashboard panel
  public void loadOutPanel () {   

    double eff_area_k = (current_part == strut)
      ? (1-alt/100) // typically only 30% of mast sits in the water
      : 1;

    // 1a. cimpute lift
    if (!foil_is_cylinder_or_ball(current_part.foil)) { 

      // Lift force
      // Legacy converion: first we produce lift in lbs, then convert to N
      // lift = current_part.cl * q0_EN * area / lconv / lconv; 
      // lift = lift * fconv;
      // double lift_si = current_part.cl * 0.5 * 1027 * velocity * velocity * 0.277778 * 0.277778 * area;
        
      double force_k = q0_SI * eff_area_k * current_part.area;
      current_part.lift = current_part.cl * force_k;
      current_part.moment = current_part.cm * force_k * current_part.chord; // according to Gudmundsson, "General Aviation..", App C1
    }
    else { // cylinder and ball

      // note: legacy 'conv' style....
      current_part.lift = rho_EN * velocity/vconv * solver.gamval * velocity/vconv * current_part.span/lconv; // lift lbs
      if (current_part.foil == FOIL_BALL) 
        current_part.lift = current_part.lift * 4.0 * solver.rval / (current_part.span/lconv) / 3.0; 

      current_part.lift *= fconv;
      current_part.cl = (current_part.lift/fconv) / ( q0_EN *  current_part.area/lconv/lconv);
    }


    // 1b. compute drag and adjust
    {
      // old FoilSimIII way was: drag = current_part.cd * q0_EN * area / lconv / lconv;
      double k_drag = q0_SI * current_part.area * eff_area_k;
      current_part.drag = current_part.cd * k_drag;
      current_part.drag_profile = current_part.cd_profile * k_drag;
      // mainly induced drag
      current_part.drag_aux = current_part.cd_aux * k_drag;

      // more ajusments...

      double thickness_0to1 = current_part.thickness * 0.01;
      double thickness_m = thickness_0to1 * current_part.chord;
 
      // Hoerner junction drag, see the formula in"Moth Full Scale..."  we produce
      // this for every part.  note the contribution is somewhat small
      // and with spray drag can be omitted in principle.
      double CDt = 17*thickness_0to1*thickness_0to1 -.05;
      if (CDt > 0) {
        double JD = CDt * q0_SI * thickness_m * thickness_m;
        if (current_part != null) current_part.drag_junc = JD;
      } else
        if (current_part != null) current_part.drag_junc = 0;

      if (current_part == strut) {
        // wave and spray. See "Moth Full Scacle Measurm... page 8" where 
        // Cdws = Rt / (0.5 * prho * v^2 * t^2) = 0.24 on average at 20fps = 6.096 m/s. 
        // knowing that average t for struts is 0.1, we have 
        // Rt = (* 0.24 (* 0.5 1027 6.096 6.096 0.012 0.012)) 0.6594837494169601 N
        // note experimental data has 0.3 on avarage, so it is used below..
        // note also that from anorher source, Cds = Cf * (7.68 - 6.4(t/c)) for vertical struts
        // example: (* 0.02 (- 7.68 (* 6.4 (/ 0.1 0.1))b)) -> 0.025
        // hwo to use it? ad Cd additive?
        double wave_spray = 0.3 * q0_SI * thickness_m * thickness_m;
        current_part.drag += wave_spray;
        current_part.drag_spray = wave_spray;
      }

    }

    liftOverDrag = Math.abs(current_part.cl/current_part.cd);


    // part 2. update GUI


    if (can_do_gui_updates) {
      con.outlft_setText((lftout == 1)
                         ? // Cl
                         pprint(filter3(current_part.cl))
                         : // lift force
                         make_force_info_in_display_units(current_part.lift, true));


      con.outDrag_setText((dragOut == 1)
                          ? // Cd
                          pprint(filter3(current_part.cd))
                          : // drag force
                          make_force_info_in_display_units(current_part.drag, true));

      con.outLD_setText(pprint(filter1(liftOverDrag)));
      con.outReynolds_setText(pprint(filter0(current_part.reynolds)));

      // TODO: maybe minimize re-update more? This can be placed in
      // spots that directly afftect the data (parts geom, VPP targets etc)
      
      out.perfweb.updateReport(); 
    }


    // need it here? if (current_part != null) current_part.save_state();
    // moving this to recomp_all_parts!!!!
    // updateTotals();

    //track_current_part.cl_changes();

    if (con.recom_all_parts_reentry_count == 0 ) 
      // save time doing incremental updates. 
      updateTotals();

  }

  public void loadProbe () {   // probe output routine

    pbval = 0.0;
    if (pboflag == 1) pbval = vel * velocity;           // velocity
    if (pboflag == 2) pbval = ((ps0 + pres * q0_EN)/2116.) * pconv; // pressure
 
    out.probe.r.l2.repaint();
    return;
  }

  double total_drag () {
    if (alt > 0) 
      board_drag = 0; // flying!
    else { 
      // this is based on "Moth Full Scale Measurements..." page 3.
      // at low speeds, board drags as classic displacing hull, then
      // at ~ 6-8 kmh the foil lift starts to contribute significantly, reducing drag
      // and after 10-11 kmh it goes down to zero fast.
      // 
      // TODO compute drag for (load-lift). why: the approach below
      // works oK, does nto use the lift, though...  one ide of using
      // lift is define 180 or 240lb Moth drag as "baseline", and
      // apprximate it as linear at 15lb per 10kmh slope and flat
      // after reaching, say 25-30 or so, and indicating planing.
      // Subtract from thsi wing lift.
      //
      double board_drag_lbs_for_240lbs = 0; 
      if (velocity <= 6) board_drag_lbs_for_240lbs = velocity/2;
      else if (velocity <= 8) board_drag_lbs_for_240lbs = 3 + (velocity-6)*1.5;
      else if (velocity <= 10) board_drag_lbs_for_240lbs = 6 + (velocity-8)*0.5; // foil starts offloading a lot
      else board_drag_lbs_for_240lbs = Math.max(0, 7 - Math.pow(velocity-10, 2)*0.5); // takeoff
      double in_newtons = 4.448222 
        * 3 // boards drag more than moth hull due to much lower Froude number 
        * board_drag_lbs_for_240lbs 
        * (load/1067);
      // System.out.println("-- in_newtons: " + in_newtons);
      board_drag = in_newtons;
    }
    return board_drag +
      wing.drag + stab.drag + fuse.drag + strut.drag +
      wing.drag_junc + stab.drag_junc + fuse.drag_junc + strut.drag_junc + strut.drag_spray;
  }
 
  // total *vertical* lift (excluded 'lift' of the strut/mast
  double total_lift () {
    return wing.lift + stab.lift + fuse.lift;
  }

  // This computes location of the center of gravity of the craft
  // (rider, roughly) in relation to the leading edge (aka LE) of the
  // mast (roughly, front bolt of DT). Positive value towards the
  // nose. This is for 'historic' reasons only; 
  // Also for 'historic' reasons, positive moments here are CCW. This is
  // opposite from standard literature discussion of aircraft pitching moments where
  // positive moments rotate the craft CW. If this is all fixed, then
  // cg_xpos would align with the x axis and be positive towards tail.
  //
  // to find combing CG pos, call it as find_cg_xpos(total_load_minus_foil_wt, ..... 0)
  //
  // to find rider body CG pos, call it as find_cg_xpos(rider_weight, ..... board_and_rig_moments)
  // 

  double center_of_lift = 0.25;
  double find_cg_xpos (double load, double driving_force, double driving_arm, double Mdrag_strut, double Mboard_wt_plus_Mrig_wt) {
    // note: looking at the left side, postive M is CCW, so Mdrag_strut is always negative
    if (Mdrag_strut > 0) Mdrag_strut = -Mdrag_strut;
    double Mdrive = // kite or sail, always CCW
      driving_force * driving_arm;

    // Mcg + Mdrive + Mdrag_strut + Mwing + Mstab + Mfuse = 0 =>
    // load_xpos * load =  - (Mwing + Mstab + Mfuse)

    //double ref_pt_fore_fuse = 100.0;
    double mast_le_xpos = strut.xpos; 
    double foils_Cm_moments =            
      wing.moment +
      stab.moment + 
      fuse.moment;

    // debug...
    // double fuse_arm = (0.25 * fuse.chord - mast_le_xpos);
    // System.out.println("-- fuse_arm: " + fuse_arm);

    double total_pitchin_moment_no_load =
      Mdrag_strut + Mdrive +
      Mboard_wt_plus_Mrig_wt +
      // if we include prorated rider-mast calcs here 
      // -(load*load_distribution_to_mast_loc) * (-mast_foot_dist_from_strut_le) +
      wing.lift * (wing.xpos + wing.chord_xoffs + center_of_lift * wing.chord - mast_le_xpos) +
      stab.lift * (stab.xpos + stab.chord_xoffs + center_of_lift * stab.chord - mast_le_xpos) +
      fuse.lift * (center_of_lift * fuse.chord - mast_le_xpos)
      // these are 'standard' moments, based on the sign of current_part.cm,  so must be taken in with negation
      -foils_Cm_moments
      ;
    
    double load_xpos = // initially, x pos in relation to mast LE at the fuse level
      - (total_pitchin_moment_no_load)
      / (load
         // if we include prorated rider-mast calcs here 
         //*(1-load_distribution_to_mast_loc)
         );

    // double load_xpos_no_moments = - (total_pitchin_moment_no_load + foils_Cm_moments)/load;
    // System.out.println("-- load_xpos_no_moments - load_xpos " + (load_xpos_no_moments-load_xpos));

    
    // now, must correct xpos for craft tilt
    // so that the value corresponds to mast LE at board deck level
    double deck_height = 0.1 // board thickness, m
      + strut.span;
    // note this offset is positive when nose is up, negative when nose is down
    double x_offset = deck_height * Math.sin(Math.toRadians(craft_pitch));

    return load_xpos + x_offset;
  }

  // just for a test
  // double find_cg_xpos_aft(double load, double tippng_mom) {
  //   // Mload + Mwing + Mstab + Mfuse + tippng_mom =  0
  //   double ref_pt_aft_fuse = 10.0;
  //   
  //   return (-tippng_mom +
  //           wing.lift * (ref_pt_aft_fuse + fuse.chord - 0.3 * wing.chord) +
  //           stab.lift * (ref_pt_aft_fuse + 0.7 * stab.chord) +
  //           fuse.lift * (ref_pt_aft_fuse + 0.7 * fuse.chord))
  //     / load - (ref_pt_aft_fuse + fuse.chord);
  // }

  class Solver {
    double ycval, xcval, gamval, rval;

    double lyg,lrg,lthg,lxgt,lygt,lrgt,lthgt;/* MOD 20 Jul */
    double lxm,lym,lxmt,lymt,vxdir;/* MOD 20 Jul */

    Solver () {}

    // aoa_degr: angle of attack in degrees
    // thickness_pst: typically current_part.thickness
    // camber_pst   : typically current_part.camber/25
    Solver (double aoa_degr, double thickness_pst, double camber_pst) { 
      this.getFreeStream(); // thsi grabs globals such as velocity, height, planet
      this.getCirculation(aoa_degr, thickness_pst, camber_pst);
      // solver.compute_foil_geometry(aoa_degr);
    }

    public String toString () {
      return "{Solver xcval: " + xcval + " ycval: " + ycval + " gamval: " + gamval + " rval: " + rval + "}";
    }

    public void setDefaults () {

      planet = 2;
      lunits = METRIC;
      lftout = 0;

      // setFoil(FOIL_JOUKOWSKI);
      //current_part.thickness/25 = .5;
      //current_part.thickness = 12.5;                   /* MODS 10 SEP 99 */
      //current_part.camber/25 = 0.0;
      //current_part.camber = 0.0;
      //current_part.aoa = 5.0;

      gamval = 0.0;
      radius = 1.0;
      spin = 0.0;
      spindr = 1.0;
      rval = 1.0;
      ycval = 0.0;
      xcval = 0.0;
      displ   = DISPLAY_ANIMATION;                            
      viewflg = VIEW_FORCES;
      current_part.cd = 0;
 
      xpval = 2.1;
      ypval = -.5;
      pboflag = 0;
      xflow = -10.0;                             /* MODS  20 Jul 99 */

      pmin = .5;
      pmax = 1.0;
      fmax = 100000.;
      fmaxb = .50;
      velocity = 20.; // km h
      vmax = 70.;

      alt = 70; // 70%
      altmax = strut.span;
      // chord = 0.1;
      //span_old = span = 1;
      //aspect_rat = 10.0;
      //arold = area = chord*span;
      // armax = 2500.01;
      // armin = .01;                 /* MODS 9 SEP 99 */
 
      xt = 170;  yt = 105; fact = 1500.0;
      zoom_slider_pos_y = 100;

      // current_part.spanfac = (int)(2.0*fact*current_part.aspect_rat*.3535);
      xt1 = xt + current_part.spanfac;
      yt1 = yt - current_part.spanfac;
      xt2 = xt - current_part.spanfac;
      yt2 = yt + current_part.spanfac;
 
      stall_model_type = STALL_MODEL_DFLT;
      v_min = 1;     v_max = 70.0;
      alt_min = 0.0;    alt_max = 85;
      ang_min = -20.0; ang_max = 20.0;
      ca_min = -20.0;  ca_max = 20.0;
      thk_min = 1; // do not make it less than 1, or some Cl?Cd calcs fail, see Solver
      thk_max = 20.0;
      chrd_min = .01;  chrd_max = 1;
      span_min = .01;  span_max = 2.5;
      ar_min = chrd_min*span_min;  ar_max = span_max*chrd_max;
      spin_min = -1500.0;   spin_max = 1500.0;
      rad_min = .05;   rad_max = 5.0;

      load_stall_model_cache(FOIL_JOUKOWSKI);

      return;
    }

    void load_stall_model_cache (Foil foil) {
      if (current_part.foil == FOIL_NACA4) {
        // these k0,k1,k2 align with the ideal aoa line at +- 5 degrees
        // see foilsim-airfoil-stall-correction.xlsx
        // stall_model_k0 = 0.83;
        // stall_model_k1 = 0.06;
        // stall_model_k2 = -0.0052;         
        // stall_model_apos = 5;
        // stall_model_aneg = -5;

        // these k0,k1,k2 align with the ideal aoa line at +- 9 degrees
        // see foilsim-airfoil-stall-correction.xlsx
        stall_model_k0 = 0.8812;
        stall_model_k1 = 0.06;
        stall_model_k2 = -0.0052;         
        stall_model_apos = 9;
        stall_model_aneg = -9;

        // these k0,k1,k2 align with the ideal aoa line at +- 10 degrees
        // see foilsim-airfoil-stall-correction.xlsx
        // stall_model_k0 = 0.86;
        // stall_model_k1 = 0.058;
        // stall_model_k2 = -0.0044;         
        // stall_model_apos = 10;
        // stall_model_aneg = -10;
      } else {
        stall_model_k0 = 0.5;
        stall_model_k1 = 0.1;
        stall_model_k2 = -0.005;
        stall_model_apos = 10;
        stall_model_aneg = -10;
      }
    }

    // free stream conditions
    // call only after planet, height or velocity changed
    void getFreeStream () {    
      /* MODS  19 Jan 00  whole routine*/
      double rgas = 1716.;                /* ft2/sec2 R */
      double gama = 1.4;
      double height_m = alt/lconv;
      double mu0 = .000000362;
      double g0 = 32.2;

      switch (planet) {
      case 0: // Earth  standard day
        if (height_m <= 36152.) {           // Troposphere
          ts0 = 518.6 - 3.56 * height_m/1000.;
          ps0 = 2116. * Math.pow(ts0/518.6,5.256);
        } else if (height_m >= 36152. && height_m <= 82345.) {   // Stratosphere
          ts0 = 389.98;
          ps0 = 2116. * .2236 *
            Math.exp((36000.-height_m)/(53.35*389.98));
        } else if (height_m >= 82345.) {
          ts0 = 389.98 + 1.645 * (height_m-82345)/1000.;
          ps0 = 2116. *.02456 * Math.pow(ts0/389.98,-11.388);
        }
         
        { 
          double temf = ts0 - 459.6;
          if (temf <= 0.0) temf = 0.0; 
          rho_EN = ps0/(rgas * ts0);
          rho_SI = 1.225; // kg/m3
                   
          /* Eq 1:6A  Domasch  - effect of humidity 
           */
          double pvap = rlhum*(2.685+.00354*Math.pow(temf,2.245))/100.;
          rho_EN = (ps0 - .379*pvap)/(rgas * ts0); 
          viscos = mu0 * 717.408/(ts0 + 198.72)*Math.pow(ts0/518.688,1.5);
        }
        break;

      case 1:   // Mars - curve fit of orbiter data
        rgas = 1149.;                /* ft2/sec2 R */
        gama = 1.29;
        rlhum = 0.0;

        if (height_m <= 22960.) {
          ts0 = 434.02 - .548 * height_m/1000.;
          ps0 = 14.62 * Math.pow(2.71828,-.00003 * height_m);
        }
        if (height_m > 22960.) {
          ts0 = 449.36 - 1.217 * height_m/1000.;
          ps0 = 14.62 * Math.pow(2.71828,-.00003 * height_m);
        }
        rho_EN = ps0/(rgas*ts0);
        rho_SI = 515.378819 * rho_EN;
        viscos = mu0 * 717.408/(ts0 + 198.72)*Math.pow(ts0/518.688,1.5);
        break;

      case 2: // water --  constant density
        height_m = -alt/lconv;
        rlhum = 100.;

        ts0 = 520.;
        // for oceanic water (it is 1027 kg/m3). FoilSimIII has it as 1.9927 slug/ft3
        // One slug has a mass of 32.174049 lb or 14.593903 kg 
        rho_EN = 1.9927; 
        rho_SI = 1027; // this is rho_EN * 515.378819

        ps0 = (2116. - rho_EN * g0 * height_m);
        mu0 = .0000272;
        viscos = mu0 * 717.408/(ts0 + 198.72)*Math.pow(ts0/518.688,1.5);
        break;
      case 3:  // specify air temp and pressure 
        rho_EN = ps0/(rgas*ts0);
        // reiplot_trace_counto temf
        {
          double temf = ts0 - 459.6;
          double pvap = rlhum*(2.685+.00354*Math.pow(temf,2.245))/100.;
          rho_EN = (ps0 - .379*pvap)/(rgas * ts0); 
        }

        rho_SI = 515.378819 * rho_EN;
        viscos = mu0 * 717.408/(ts0 + 198.72)*Math.pow(ts0/518.688,1.5);
        break;

      case 4:  // specify fluid density and viscosity
        rlhum = 0.0;
        ps0 = 2116.;
        break;

      case 5: // Venus - surface conditions
        rgas = 1149.;                
        gama = 1.29;
        rlhum = 0.0;

        ts0 = 1331.6;
        ps0 = 194672.;

        rho_EN = ps0/(rgas*ts0);
        rho_SI = 515.378819 * rho_EN;
        viscos = mu0 * 717.408/(ts0 + 198.72)*Math.pow(ts0/518.688,1.5);
      default:
      }

      set_q0();
      // pt0 = ps0 + q0_EN;

      return;
    }

    void set_q0 () {
      q0_EN  = .5 * rho_EN * velocity * velocity / (vconv * vconv);
      q0_SI = .5 * rho_SI * velocity * velocity * 0.277778 * 0.277778; // velocity from kmh to m/s
    }

    // circulation from Kutta condition
    // thickness_pst: typically current_part.thickness
    // camber_pst   : typically current_part.camber/25
    void getCirculation (double effaoa, double thickness_pst, double camber_pst) {   
      double beta;
      double thickness = thickness_pst/100.0; 
      double camber = camber_pst/100.0; 
      xcval = 0.0;
      ycval = 2 * camber; // was: current_part.camber/25/2, which is current_part.camber/25.

      if (current_part.foil == FOIL_CYLINDER ||      /* get circulation for rotating cylnder */
          current_part.foil == FOIL_BALL) {         /* get circulation for rotating ball */
        rval = radius/lconv;
        gamval = 4.0 * 3.1415926 * 3.1415926 *spin * rval * rval
          / (velocity/vconv);
        gamval = gamval * spindr;
        ycval = .0001;
      } else {
        rval = thickness + Math.sqrt(thickness*thickness + ycval*ycval + 1.0);
        xcval = 1.0 - Math.sqrt(rval*rval - ycval*ycval);
        beta = Math.asin(ycval/rval)/convdr;     /* Kutta condition */
        gamval = 2.0*rval*Math.sin((effaoa+beta)*convdr);
      }
    }
    
    double get_Cl (double effaoa) {
      double result = current_part.foil.get_Cl(effaoa);

      if (ar_lift_corr) {  // correction for low aspect ratio
        result = result /(1.0 + Math.abs(result)/(3.14159*current_part.aspect_rat));
      }
      return result;
    }

    double compute_Cl_Kutta (double effaoa) {
      double stfact, leg,teg,lem,tem;

      double result;
      /*  compute lift coefficient */
      leg = xcval - Math.sqrt(rval*rval - ycval*ycval);
      teg = xcval + Math.sqrt(rval*rval - ycval*ycval);
      lem = leg + 1.0/leg;
      tem = teg + 1.0/teg;

      double chrd = tem - lem;
      // stall model
      stfact = 1.0;
      switch (stall_model_type) {
      case STALL_MODEL_DFLT:
        if (effaoa > 10 ) {
          stfact = .5 + .1 * effaoa - .005 * effaoa * effaoa;
        } else if (effaoa < -10 ) {
          stfact = .5 - .1 * effaoa - .005 * effaoa * effaoa;
        }
        break;
      case STALL_MODEL_REFINED:
        // stfact = stall_model_k0 + stall_model_k1 * Math.abs(effaoa) + stall_model_k2 * effaoa * effaoa;
        if (effaoa > stall_model_apos ) {
          stfact = stall_model_k0 + stall_model_k1 * +effaoa + stall_model_k2 * effaoa * effaoa;
        } else if (effaoa < stall_model_aneg ) {
          stfact = stall_model_k0 + stall_model_k1 * -effaoa + stall_model_k2 * effaoa * effaoa;
        }
        break;
      case STALL_MODEL_IDEAL_FLOW:
      default:
        // do nothing. stfact = 1
      }
      
      result = stfact *
        gamval*4.0*3.1415926/chrd;
        
      return result;
    }
    
    void compute_foil_geometry (double effaoa) {
      double thet, rdm, thtm;
      // System.out.println("-- compute_foil_geometry: " + effaoa);
      int index;

      // geometry of Joukowski shape is in [0][index]
      for (index =1; index <= POINTS_COUNT; ++index) {
        thet = (index -1)*360./(POINTS_COUNT-1);
        xg[0][index] = rval * Math.cos(convdr * thet) + xcval;
        yg[0][index] = rval * Math.sin(convdr * thet) + ycval;
        rg[0][index] = Math.sqrt(xg[0][index]*xg[0][index] +
                                 yg[0][index]*yg[0][index]);
        thg[0][index] = Math.atan2(yg[0][index],xg[0][index])/convdr;
        xm[0][index] = (rg[0][index] + 1.0/rg[0][index])*
          Math.cos(convdr*thg[0][index]);
        ym[0][index] = (rg[0][index] - 1.0/rg[0][index])*
          Math.sin(convdr*thg[0][index]);
        rdm = Math.sqrt(xm[0][index]*xm[0][index] +
                        ym[0][index]*ym[0][index]);
        thtm = Math.atan2(ym[0][index],xm[0][index])/convdr;
        xm[0][index] = rdm * Math.cos((thtm - effaoa)*convdr);
        ym[0][index] = rdm * Math.sin((thtm - effaoa)*convdr);
        getVel(rval,thet);
        plp[index] = ((ps0 + pres * q0_EN)/2116.) * pconv;
        plv[index] = vel * velocity;
      }

      // scale up and rotate the imported geom shape
      if (current_part.foil.geometry != null) {
        Point2D[] g = current_part.foil.geometry;
        double rad_aoa = -Math.toRadians(effaoa);
        for (index = 0; index < 61; ++ index) {
          Point2D pt = g[(int)(index)];
          //the coordinates (x2,y2) of the point (x,y) after rotation are
          //x2 = x cos a - y  sin a 
          //y2 = x sin a + y cos a 
          double x = 3.5 * (pt.x - 0.5);
          double y = 3.5 * pt.y;
          double rot_x = x*Math.cos(rad_aoa) - y*Math.sin(rad_aoa);
          double rot_y = x*Math.sin(rad_aoa) + y*Math.cos(rad_aoa);
          xm[0][index] = rot_x;
          ym[0][index] = rot_y;
        }
      }

      xt1 = xt + current_part.spanfac;
      yt1 = yt - current_part.spanfac;
      xt2 = xt - current_part.spanfac;
      yt2 = yt + current_part.spanfac;

    }


    public void genFlow (double effaoa) {   // generate flowfield
      double rnew,thet,psv,fxg;
      //System.out.println("------------- genFlow: ");
      int k,index;

      /* all lines of flow  except stagnation line*/
      for (k=1; k<=STREAMLINES_COUNT; ++k) {
        psv = -.5*(STREAMLINES_COUNT_HALF-1) + .5*(k-1);
        fxg = xflow;
        // System.out.println("-- psv: " + psv + "-- fxg: " + fxg);
        for (index =1; index <=POINTS_COUNT; ++ index) {
          getPoints(fxg,psv);
          xg[k][index]  = lxgt;
          yg[k][index]  = lygt;
          rg[k][index]  = lrgt;
          thg[k][index] = lthgt;
          xm[k][index]  = lxmt;
          ym[k][index]  = lymt;

          if (stall_model_type != STALL_MODEL_IDEAL_FLOW) {           // stall model
            double apos = stall_model_type == STALL_MODEL_DFLT ? +10 : stall_model_apos;
            double aneg = stall_model_type == STALL_MODEL_DFLT ? -10 : stall_model_aneg;
            if (effaoa > apos && psv > 0.0) {
              if (xm[k][index] > 0.0) {
                ym[k][index] = ym[k][index -1];
              }
            }
            if (effaoa < aneg && psv < 0.0) {
              if (xm[k][index] > 0.0) {
                ym[k][index] = ym[k][index -1];
              }
            }
          }
          solver.getVel(lrg,lthg);
          fxg = fxg + vxdir*STEP_X;
        }
      }
      /*  stagnation line */
      k = STREAMLINES_COUNT_HALF;
      psv = 0.0;
      /*  incoming flow */
      for (index =1; index <= POINTS_COUNT_HALF; ++ index) {
        rnew = 10.0 - (10.0 - rval)*Math.sin(pid2*(index-1)/(POINTS_COUNT_HALF-1));
        thet = Math.asin(.999*(psv - gamval*Math.log(rnew/rval))/
                         (rnew - rval*rval/rnew));
        fxg =  - rnew * Math.cos(thet);
        getPoints(fxg,psv);
        xg[k][index]  = lxgt;
        yg[k][index]  = lygt;
        rg[k][index]  = lrgt;
        thg[k][index] = lthgt;
        xm[k][index]  = lxmt;
        ym[k][index]  = lymt;
      }
      /*  downstream flow */
      for (index = 1; index <= POINTS_COUNT_HALF; ++ index) {
        rnew = 10.0 + .01 - (10.0 - rval)*Math.cos(pid2*(index-1)/(POINTS_COUNT_HALF-1));
        thet = Math.asin(.999*(psv - gamval*Math.log(rnew/rval))/
                         (rnew - rval*rval/rnew));
        fxg =   rnew * Math.cos(thet);
        getPoints(fxg,psv);
        xg[k][POINTS_COUNT_HALF+index]  = lxgt;
        yg[k][POINTS_COUNT_HALF+index]  = lygt;
        rg[k][POINTS_COUNT_HALF+index]  = lrgt;
        thg[k][POINTS_COUNT_HALF+index] = lthgt;
        xm[k][POINTS_COUNT_HALF+index]  = lxmt;
        ym[k][POINTS_COUNT_HALF+index]  = lymt;
      }
      /*  stagnation point */
      xg[k][POINTS_COUNT_HALF]  = xcval;
      yg[k][POINTS_COUNT_HALF]  = ycval;
      rg[k][POINTS_COUNT_HALF]  = Math.sqrt(xcval*xcval+ycval*ycval);
      thg[k][POINTS_COUNT_HALF] = Math.atan2(ycval,xcval)/convdr;
      xm[k][POINTS_COUNT_HALF]  = (xm[k][POINTS_COUNT_HALF+1] + xm[k][POINTS_COUNT_HALF-1])/2.0;
      ym[k][POINTS_COUNT_HALF]  = (ym[0][POINTS_COUNT/4+1] + ym[0][POINTS_COUNT/4*3+1])/2.0;

    }

    public void getPoints (double fxg, double psv) {   // flow in x-psi
      double radm,thetm;                /* MODS  20 Jul 99  whole routine*/
      double fnew,y_new,y_old,rfac;
      double xold,xnew,thet;
      double rmin,rmax;
      int iter,isign;
      double effaoa = effective_aoa();

      /* get variables in the generating plane */
      /* iterate to find value of yg */
      y_new = 10.0;
      y_old = 10.0;
      if (psv < 0.0) y_new = -10.0;
      if (Math.abs(psv) < .001 && effaoa < 0.0) y_new = rval;
      if (Math.abs(psv) < .001 && effaoa >= 0.0) y_new = -rval;
      fnew = 0.1;
      iter = 1;
      while (Math.abs(fnew) >= .00001 && iter < 25) {
        ++iter;
        rfac = fxg*fxg + y_new*y_new;
        if (rfac < rval*rval) rfac = rval*rval + .01;
        fnew = psv - y_new*(1.0 - rval*rval/rfac)
          - gamval*Math.log(Math.sqrt(rfac)/rval);
        double deriv = - (1.0 - rval*rval/rfac)
          - 2.0 * y_new*y_new*rval*rval/(rfac*rfac)
          - gamval * y_new / rfac;
        y_old = y_new;
        y_new = y_old  - .5*fnew/deriv;
      }
      lyg = y_old;
      /* rotate for angle of attack */
      lrg = Math.sqrt(fxg*fxg + lyg*lyg);
      lthg = Math.atan2(lyg,fxg)/convdr;
      lxgt = lrg * Math.cos(convdr*(lthg + effaoa));
      lygt = lrg * Math.sin(convdr*(lthg + effaoa));
      /* translate cylinder to generate airfoil */
      lxgt = lxgt + xcval;
      lygt = lygt + ycval;
      lrgt = Math.sqrt(lxgt*lxgt + lygt*lygt);
      lthgt = Math.atan2(lygt,lxgt)/convdr;
      /*  Kutta-Joukowski mapping */
      lxm = (lrgt + 1.0/lrgt)*Math.cos(convdr*lthgt);
      lym = (lrgt - 1.0/lrgt)*Math.sin(convdr*lthgt);
      /* tranforms for view fixed with free stream */
      /* take out rotation for angle of attack mapped and cylinder */
      radm = Math.sqrt(lxm*lxm+lym*lym);
      thetm = Math.atan2(lym,lxm)/convdr;
      lxmt = radm*Math.cos(convdr*(thetm-effaoa));
      lymt = radm*Math.sin(convdr*(thetm-effaoa));

      lxgt = lxgt - xcval;
      lygt = lygt - ycval;
      lrgt = Math.sqrt(lxgt*lxgt + lygt*lygt);
      lthgt = Math.atan2(lygt,lxgt)/convdr;
      lxgt = lrgt * Math.cos((lthgt - effaoa)*convdr);
      lygt = lrgt * Math.sin((lthgt - effaoa)*convdr);

      return;
    }
 
    public void getVel (double rad, double theta) {  //velocity and pressure 
      double ur,uth,jake1,jake2,jakesq;
      double xloc,yloc,thrad,alfrad;
      double effaoa = effective_aoa();

      thrad = convdr * theta;
      alfrad = convdr * effaoa;
      /* get x, y location in cylinder plane */
      xloc = rad * Math.cos(thrad);
      yloc = rad * Math.sin(thrad);
      /* velocity in cylinder plane */
      ur  = Math.cos(thrad-alfrad)*(1.0-(rval*rval)/(rad*rad));
      uth = -Math.sin(thrad-alfrad)*(1.0+(rval*rval)/(rad*rad))
        - gamval/rad;
      usq = ur*ur + uth*uth;
      vxdir = ur * Math.cos(thrad) - uth * Math.sin(thrad); // MODS  20 Jul 99 
      /* translate to generate airfoil  */
      xloc = xloc + xcval;
      yloc = yloc + ycval;
      /* compute new radius-theta  */
      rad = Math.sqrt(xloc*xloc + yloc*yloc);
      thrad  = Math.atan2(yloc,xloc);
      /* compute Joukowski Jacobian  */
      jake1 = 1.0 - Math.cos(2.0*thrad)/(rad*rad);
      jake2 = Math.sin(2.0*thrad)/(rad*rad);
      jakesq = jake1*jake1 + jake2*jake2;
      if (Math.abs(jakesq) <= .01) jakesq = .01;  /* protection */
      vsq = usq / jakesq;
      /* vel is velocity ratio - pres is coefficient  (p-p0)/q0_EN   */
      if (!foil_is_cylinder_or_ball(current_part.foil)) {
        vel = Math.sqrt(vsq);
        pres = 1.0 - vsq;
      } else  {
        vel = Math.sqrt(usq);
        pres = 1.0 - usq;
      }
      return;
    }

    public void getProbe () { /* all of the information needed for the probe */
      double prxg;
      int index;
      double effaoa = effective_aoa();
      /* get variables in the generating plane */
      if (Math.abs(ypval) < .01) ypval = .05;
      getPoints(xpval,ypval);

      solver.getVel(lrg,lthg);
      loadProbe();

      pxg = lxgt;
      pyg = lygt;
      prg = lrgt;
      pthg = lthgt;
      pxm = lxmt;
      pym = lymt;
      /* smoke */
      if (pboflag == 3 ) {
        prxg = xpval;
        for (index =1; index <=POINTS_COUNT; ++ index) {
          getPoints(prxg,ypval);
          xg[19][index] = lxgt;
          yg[19][index] = lygt;
          rg[19][index] = lrgt;
          thg[19][index] = lthgt;
          xm[19][index] = lxmt;
          ym[19][index] = lymt;
          if (stall_model_type != STALL_MODEL_IDEAL_FLOW) {           // stall model
            double apos = stall_model_type == STALL_MODEL_DFLT ? +10 : stall_model_apos;
            double aneg = stall_model_type == STALL_MODEL_DFLT ? -10 : stall_model_aneg;
            if (xpval > 0.0) {
              if (effaoa > apos  && ypval > 0.0) { 
                ym[19][index] = ym[19][1];
              } 
              if (effaoa < aneg && ypval < 0.0) {
                ym[19][index] = ym[19][1];
              }
            }
            if (xpval < 0.0) {
              if (effaoa > apos && ypval > 0.0) { 
                if (xm[19][index] > 0.0) {
                  ym[19][index] = ym[19][index-1];
                }
              } 
              if (effaoa < aneg && ypval < 0.0) {
                if (xm[19][index] > 0.0) {
                  ym[19][index] = ym[19][index-1];
                }
              }
            }
          }
          solver.getVel(lrg,lthg);
          prxg = prxg + vxdir*STEP_X;
        }
      }
      return;
    }

    // from http://paulbourke.net/miscellaneous/interpolation/ 
    double LinearInterpolate (double y1,double y2, double mu) {
      double res = (y1*(1-mu)+y2*mu);
      System.out.println("-- y1: " + y1);
      System.out.println("-- y2: " + y2);
      System.out.println("-- res: " + res);
      return res;
    }

    // from http://paulbourke.net/miscellaneous/interpolation/ 
    // The
    // function requires 4 points in all labeled y0, y1, y2, and y3,
    // mu: 0 to 1 for 
    // interpolating between the segment y1 to y2. 
    double CubicInterpolate (double y0,double y1,
                            double y2,double y3,
                            double mu)
    {
      double a0,a1,a2,a3,mu2;

      mu2 = mu*mu;

      //a0 = y3 - y2 - y0 + y1;
      //a1 = y0 - y1 - a0;
      //a2 = y2 - y0;
      //a3 = y1;

      // Paul Breeuwsma proposes the following coefficients for a
      // smoother interpolated curve, which uses the slope between the
      // previous point and the next as the derivative at the current
      // point. This results in what are generally referred to as
      // Catmull-Rom splines.
      a0 = -0.5*y0 + 1.5*y1 - 1.5*y2 + 0.5*y3;
      a1 = y0 - 2.5*y1 + 2*y2 - 0.5*y3;
      a2 = -0.5*y0 + 0.5*y2;
      a3 = y1;

      return(a0*mu*mu2+a1*mu2+a2*mu+a3);
    }

    // Name = AQUILA 9.3% smoothed
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finis    // h = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      //     Cd       Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     //     [-]      [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.1    // 25   0.39381 -0.021  0.893   0.006   0.931   0.015   -0.317  0.278   0.082
    // -24.0    -0.1    // 63   0.29085 -0.020  0.887   0.007   0.931   0.014   -0.561  0.272   0.128
    // -20.0    -0.2    // 16   0.21866 -0.019  0.881   0.006   0.933   0.011   -0.989  0.259   0.162
    // -16.0    -0.2    // 83   0.16381 -0.019  0.862   0.006   0.937   0.010   -1.727  0.251   0.183
    // -12.0    -0.3    // 36   0.09896 -0.019  0.823   0.006   0.943   0.009   -3.398  0.241   0.194
    // -8.0     -0.2    // 88   0.06640 -0.019  0.728   0.005   0.960   0.007   -4.342  0.253   0.184
    // -4.0     -0.0    // 45   0.03735 -0.020  0.593   0.005   0.975   0.006   -1.216  0.310   -0.186
    // -0.0     0.40    // 0    0.00847 -0.060  0.462   0.012   0.982   0.989   47.277  0.300   0.400
    // 4.0      0.86    // 4    0.00661 -0.066  0.307   0.946   0.986   0.988   130.772 0.263   0.326
    // 8.0      1.29    // 4    0.01596 -0.071  0.152   0.998   0.955   0.998   81.085  0.263   0.305
    // 12.0     1.38    // 8    0.03889 -0.072  0.002   1.000   0.635   1.000   35.696  0.403   0.302
    // 16.0     1.00    // 4    0.14560 -0.027  0.001   1.000   0.003   1.000   6.895   0.311   0.277
    // 20.0     0.68    // 5    0.24856 -0.029  0.001   1.000   0.003   1.000   2.758   0.235   0.293
    // 24.0     0.44    // 3    0.33959 -0.036  0.001   1.000   0.005   1.000   1.304   0.221   0.331
    // 28.0     0.29    // 2    0.48325 -0.041  0.001   0.999   0.007   0.999   0.604   0.218   0.389
    //                  // 
    double[] t_lift_AQUILA_9p3 = {-0.1 ,-0.1 ,-0.2 ,-0.2 ,-0.3 ,-0.2 ,-0.0 ,0.40 ,0.86 ,1.29 ,1.38 ,1.00 ,0.68 ,0.44 ,0.29 };
    double[] t_drag_AQUILA_9p3 = {0.39381, 0.29085, 0.21866, 0.16381, 0.09896, 0.06640, 0.03735, 0.00847, 0.00661, 0.01596, 0.03889, 0.14560, 0.24856, 0.33959, 0.48325 };


    // Name = NACA 63-412 AIRFOIL
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.246  0.48435 -0.015  0.945   0.002   1.000   0.011   -0.508  0.246   0.188
    // -24.0    -0.341  0.38844 -0.016  0.930   0.002   1.000   0.008   -0.878  0.237   0.204
    // -20.0    -0.474  0.25074 -0.018  0.920   0.003   1.000   0.009   -1.889  0.231   0.212
    // -16.0    -0.614  0.14833 -0.021  0.900   0.004   1.000   0.010   -4.138  0.225   0.216
    // -12.0    -0.641  0.08843 -0.022  0.845   0.007   1.000   0.008   -7.245  0.274   0.215
    // -8.0     -0.425  0.05258 -0.025  0.751   0.008   1.000   0.009   -8.089  0.338   0.190
    // -4.0     -0.087  0.01062 -0.071  0.647   0.009   1.000   0.931   -8.197  0.316   -0.567
    // -0.0     0.386   0.00814 -0.079  0.547   0.517   1.000   0.954   47.401  0.266   0.455
    // 4.0      0.856   0.01400 -0.087  0.006   0.660   1.000   0.954   61.186  0.173   0.351
    // 8.0      0.981   0.05228 -0.033  0.004   0.705   0.005   0.955   18.765  0.029   0.284
    // 12.0     1.087   0.08887 -0.036  0.005   0.838   0.005   0.957   12.228  0.935   0.283
    // 16.0     0.989   0.15306 -0.038  0.004   0.888   0.006   0.951   6.461   0.232   0.289
    // 20.0     0.708   0.22957 -0.043  0.004   0.922   0.007   0.951   3.085   0.232   0.310
    // 24.0     0.480   0.34907 -0.048  0.003   0.930   0.009   0.956   1.375   0.226   0.350
    // 28.0     0.329   0.46304 -0.052  0.002   0.930   0.011   0.956   0.710   0.223   0.408

    double[] t_lift_NACA_63_412 = {-0.246, -0.341, -0.474, -0.614, -0.641, -0.425, -0.087, 0.386 , 0.856 , 0.981 , 1.087 , 0.989 , 0.708 , 0.480 , 0.329     };

    double[] t_drag_NACA_63_412 = {0.48435, 0.38844, 0.25074, 0.14833, 0.08843, 0.05258, 0.01062, 0.00814, 0.01400, 0.05228, 0.08887, 0.15306, 0.22957, 0.34907, 0.46304  };

    // attempt to model Bladerider foil. Bladerider foil is modified NACA_63_412 with flap down.
    // the following is produced with (1) NACA_63_412 data from airfoils site inserted in javafoil
    // (2) that foil is modded with trailing gap = 1, flaps: 3- 1, 25 1, 20 1.

    // Name = NACA 63-412 AIRFOIL modded
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.264  0.45319 -0.024  0.999   0.002   0.999   0.010   -0.582  0.231   0.160
    // -24.0    -0.364  0.37355 -0.026  0.999   0.002   0.999   0.008   -0.974  0.224   0.179
    // -20.0    -0.493  0.22506 -0.030  0.999   0.003   0.999   0.010   -2.191  0.219   0.190
    // -16.0    -0.599  0.14758 -0.033  0.862   0.005   0.999   0.010   -4.055  0.135   0.195
    // -12.0    -0.542  0.09361 -0.035  0.824   0.007   0.999   0.008   -5.788  0.270   0.185
    // -8.0     -0.255  0.06026 -0.040  0.799   0.008   0.999   0.009   -4.232  0.358   0.094
    // -4.0     0.148   0.01756 -0.110  0.751   0.416   0.999   0.956   8.449   0.340   0.991
    // -0.0     0.625   0.01845 -0.119  0.568   0.520   0.999   0.958   33.893  0.269   0.440
    // 4.0      1.086   0.02570 -0.128  0.006   0.650   0.999   0.958   42.265  0.108   0.368
    // 8.0      1.124   0.06436 -0.048  0.004   0.677   0.005   0.959   17.464  -0.733  0.293
    // 12.0     1.165   0.10404 -0.051  0.005   0.699   0.005   0.960   11.194  0.216   0.294
    // 16.0     0.974   0.17417 -0.053  0.004   0.951   0.005   0.952   5.593   0.238   0.305
    // 20.0     0.676   0.24795 -0.057  0.004   0.951   0.006   0.952   2.728   0.228   0.334
    // 24.0     0.456   0.37413 -0.065  0.002   0.960   0.010   0.961   1.219   0.220   0.392
    // 28.0     0.314   0.51586 -0.068  0.002   0.960   0.011   0.961   0.608   0.229   0.466
    // 

    double[] t_lift_NACA_63_412_mod_flap_v1 = {-0.264, -0.364, -0.493, -0.599, -0.542, -0.255, 0.148 , 0.625 , 1.086 , 1.124 , 1.165 , 0.974 , 0.676 , 0.456 , 0.314   };
    double[] t_drag_NACA_63_412_mod_flap_v1 = {0.45319, 0.37355, 0.22506, 0.14758, 0.09361, 0.06026, 0.01756, 0.01845, 0.02570, 0.06436, 0.10404, 0.17417, 0.24795, 0.37413, 0.51586  };


    // Name = SD7084 (9.6%)
    // Mach = 0; Re = 700000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    // Alpha   Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    //         [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0   -0.134  0.38042 -0.012  1.000   0.002   1.000   0.022   -0.353  0.253   0.161
    // -24.0   -0.192  0.27887 -0.012  0.989   0.002   1.000   0.020   -0.690  0.251   0.189
    // -20.0   -0.285  0.20817 -0.012  0.953   0.002   1.000   0.016   -1.370  0.251   0.209
    // -16.0   -0.421  0.16472 -0.012  0.918   0.002   1.000   0.011   -2.555  0.252   0.223
    // -12.0   -0.548  0.09257 -0.011  0.882   0.003   1.000   0.006   -5.916  0.215   0.229
    // -8.0    -0.475  0.05259 -0.013  0.843   0.005   1.000   0.009   -9.030  0.330   0.222
    // -4.0    -0.214  0.00829 -0.038  0.784   0.007   1.000   0.998   -25.880 0.290   0.072
    // -0.0    0.251   0.00531 -0.043  0.581   0.766   1.000   0.998   47.285  0.260   0.420
    // 4.0     0.717   0.00687 -0.047  0.238   0.972   0.992   0.991   104.263 0.261   0.316
    // 8.0     1.111   0.01504 -0.052  0.007   0.981   0.940   0.992   73.861  0.261   0.297
    // 12.0    1.068   0.03960 -0.051  0.001   0.988   0.517   0.992   26.978  0.316   0.298
    // 16.0    0.706   0.13343 -0.025  0.001   0.989   0.009   0.993   5.293   0.288   0.286
    // 20.0    0.452   0.20179 -0.028  0.001   0.990   0.010   0.994   2.238   0.235   0.311
    // 24.0    0.286   0.29604 -0.031  0.001   0.991   0.012   0.994   0.966   0.216   0.360
    // 28.0    0.189   0.38724 -0.037  0.001   0.991   0.018   0.996   0.488   0.197   0.444


    double[] t_lift_SD7084 = {-0.134, -0.192, -0.285, -0.421, -0.548, -0.475, -0.214, 0.251, 0.717, 1.111, 1.068, 0.706, 0.452, 0.286, 0.189};
    double[] t_drag_SD7084 = {0.38042, 0.27887, 0.20817, 0.16472, 0.09257, 0.05259, 0.00829, 0.00531, 0.00687, 0.01504, 0.03960, 0.13343, 0.20179, 0.29604, 0.38724};



    // Name = SD8040 (10%)
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    // alpha   Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0   -0.214  0.42136 -0.021  0.977   0.005   0.988   0.015   -0.508  0.263   0.150
    // -24.0   -0.285  0.31478 -0.021  0.973   0.004   0.988   0.013   -0.904  0.259   0.178
    // -20.0   -0.380  0.22511 -0.020  0.958   0.004   0.988   0.011   -1.690  0.260   0.198
    // -16.0   -0.488  0.17173 -0.019  0.928   0.003   0.989   0.007   -2.845  0.262   0.212
    // -12.0   -0.543  0.09601 -0.018  0.889   0.004   0.990   0.005   -5.651  0.270   0.217
    // -8.0    -0.423  0.05525 -0.020  0.837   0.004   0.990   0.007   -7.648  0.344   0.203
    // -4.0    -0.139  0.00857 -0.056  0.758   0.011   0.992   0.996   -16.265 0.302   -0.151
    // -0.0    0.331   0.00658 -0.059  0.563   0.718   0.994   0.998   50.258  0.257   0.429
    // 4.0     0.797   0.00897 -0.063  0.252   0.956   0.990   0.992   88.852  0.258   0.329
    // 8.0     1.208   0.01785 -0.066  0.037   0.990   0.928   0.996   67.647  0.255   0.305
    // 12.0    1.391   0.03665 -0.066  0.008   0.997   0.642   0.997   37.960  0.746   0.297
    // 16.0    1.153   0.13607 -0.039  0.004   0.997   0.035   0.997   8.475   0.312   0.284
    // 20.0    0.909   0.21938 -0.036  0.002   0.997   0.017   0.997   4.145   0.261   0.290
    // 24.0    0.659   0.33062 -0.034  0.001   0.998   0.009   0.998   1.993   0.256   0.302
    // 28.0    0.470   0.48375 -0.033  0.001   0.998   0.006   0.998   0.971   0.254   0.321


    double[] t_lift_SD8040 = {-0.214, -0.285, -0.380, -0.488, -0.543, -0.423, -0.139, 0.331, 0.797, 1.208, 1.391, 1.153, 0.909, 0.659, 0.470};
    double[] t_drag_SD8040 = {0.42136, 0.31478, 0.22511, 0.17173, 0.09601, 0.05525, 0.00857, 0.00658, 0.00897, 0.01785, 0.03665, 0.13607, 0.21938, 0.33062, 0.48375};



    // NACA(camber)4(thickness) airfoil drag tables computed by
    // javafoil for NAcA 4 digit geometry and -28 to +28 step 4 AoA
    // and for Re=300000 and infine aspect ratio, Used for
    // interpolation instead of original FoilSim III polynomials.  The
    // reynolds correction and induced drag low aspect corrections of
    // FoilSim woudl hen apply (see the end of get_Cd() method).
    // Tables produced by dmitrynizh 2016.

    // Name = NACA 0005
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.034  0.41708 0.007   1.000   0.004   1.000   0.016   -0.082  0.208   0.467
    // -24.0    -0.052  0.31201 0.007   1.000   0.003   1.000   0.016   -0.167  0.214   0.377
    // -20.0    -0.086  0.22381 0.006   1.000   0.003   1.000   0.013   -0.386  0.227   0.314
    // -16.0    -0.156  0.23465 0.004   1.000   0.002   1.000   0.009   -0.667  0.238   0.277
    // -12.0    -0.302  0.12617 0.003   1.000   0.002   1.000   0.006   -2.396  0.242   0.260
    // -8.0     -0.489  0.06937 0.002   1.000   0.001   1.000   0.003   -7.043  0.212   0.254
    // -4.0     -0.353  0.03531 0.001   0.990   0.005   1.000   0.008   -9.988  0.254   0.253
    // -0.0     -0.000  0.00661 -0.000  0.909   0.909   1.000   0.999   -0.000  0.253   0.250
    // 4.0      0.353   0.03531 -0.001  0.005   0.990   0.008   0.998   9.988   0.254   0.253
    // 8.0      0.489   0.06937 -0.002  0.001   0.998   0.003   0.998   7.043   0.212   0.254
    // 12.0     0.302   0.12617 -0.003  0.002   0.998   0.006   0.998   2.396   0.242   0.260
    // 16.0     0.156   0.23465 -0.004  0.002   0.998   0.009   0.999   0.667   0.238   0.277
    // 20.0     0.086   0.22381 -0.006  0.003   0.998   0.013   0.999   0.386   0.227   0.314
    // 24.0     0.052   0.31201 -0.007  0.003   0.998   0.016   0.999   0.167   0.214   0.377
    // 28.0     0.034   0.41708 -0.007  0.004   0.998   0.016   0.998   0.082   0.208   0.467

    double[] t_lift_NACA4_0005 = {-0.034, -0.052, -0.086, -0.156, -0.302, -0.489, -0.353, -0.000, 0.353 , 0.489 , 0.302 , 0.156 , 0.086 , 0.052 , 0.034     };


    double[] t_drag_NACA4_0005 = {0.41708, 0.31201, 0.22381, 0.23465, 0.12617, 0.06937, 0.03531, 0.00661, 0.03531, 0.06937, 0.12617, 0.23465, 0.22381, 0.31201, 0.41708};

    // Name = NACA 5405
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.025  0.37907 -0.047  0.970   0.005   0.984   0.018   -0.066  0.271   -1.640
    // -24.0    -0.036  0.28615 -0.047  0.966   0.004   0.985   0.017   -0.126  0.312   -1.054
    // -20.0    -0.055  0.20679 -0.045  0.962   0.004   0.985   0.013   -0.264  0.309   -0.578
    // -16.0    -0.087  0.21957 -0.044  0.951   0.003   0.988   0.010   -0.398  0.299   -0.251
    // -12.0    -0.138  0.12871 -0.041  0.930   0.003   0.994   0.007   -1.072  0.354   -0.048
    // -8.0     -0.141  0.06818 -0.038  0.886   0.002   1.000   0.004   -2.070  0.256   -0.021
    // -4.0     0.113   0.03873 -0.043  0.807   0.004   1.000   0.006   2.924   0.363   0.628
    // -0.0     0.602   0.00810 -0.122  0.660   0.998   1.000   0.998   74.366  0.340   0.452
    // 4.0      1.039   0.01528 -0.126  0.006   0.998   1.000   0.998   67.990  -0.015  0.371
    // 8.0      0.924   0.06111 -0.037  0.001   0.999   0.002   0.999   15.128  0.411   0.290
    // 12.0     0.531   0.11562 -0.045  0.001   0.998   0.004   0.998   4.597   0.229   0.334
    // 16.0     0.250   0.21581 -0.051  0.001   0.998   0.006   0.999   1.158   0.217   0.453
    // 20.0     0.127   0.22715 -0.058  0.001   0.999   0.010   0.999   0.560   0.180   0.704
    // 24.0     0.072   0.31187 -0.063  0.002   0.999   0.014   0.999   0.232   0.149   1.126
    // 28.0     0.045   0.42785 -0.066  0.003   0.998   0.015   0.998   0.105   0.148   1.718

    double[] t_lift_NACA4_0505 = {-0.025, -0.036, -0.055, -0.087, -0.138, -0.141, 0.113 , 0.602 , 1.039 , 0.924 , 0.531 , 0.250 , 0.127 , 0.072 , 0.045     };


    double[] t_drag_NACA4_0505 = { 0.37907, 0.28615, 0.20679, 0.21957, 0.12871, 0.06818, 0.03873, 0.00810, 0.01528, 0.06111, 0.11562, 0.21581, 0.22715, 0.31187, 0.42785};

    // Name = NACA 10405
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.018  0.36031 -0.096  0.933   0.005   0.968   0.019   -0.049  0.741   -5.123
    // -24.0    -0.024  0.26584 -0.093  0.918   0.005   0.972   0.016   -0.089  0.629   -3.672
    // -20.0    -0.032  0.37840 -0.091  0.900   0.004   0.976   0.014   -0.084  0.569   -2.609
    // -16.0    -0.039  0.21784 -0.088  0.872   0.004   0.979   0.011   -0.181  -1.092  -1.976
    // -12.0    -0.027  0.12650 -0.085  0.827   0.002   0.984   0.008   -0.215  0.190   -2.856
    // -8.0     0.111   0.06993 -0.079  0.765   0.002   0.989   0.005   1.593   0.236   0.958
    // -4.0     0.538   0.04199 -0.076  0.680   0.002   0.996   0.004   12.820  0.400   0.392
    // -0.0     1.188   0.01390 -0.241  0.442   0.012   1.000   1.000   85.498  0.407   0.453
    // 4.0      1.623   0.02178 -0.247  0.008   0.999   0.965   0.999   74.544  -0.534  0.402
    // 8.0      1.402   0.05662 -0.074  0.001   0.998   0.002   0.998   24.755  0.458   0.303
    // 12.0     0.812   0.10739 -0.078  0.000   0.998   0.002   0.998   7.561   0.232   0.346
    // 16.0     0.363   0.19528 -0.093  0.001   0.998   0.004   0.998   1.860   0.208   0.505
    // 20.0     0.175   0.34912 -0.104  0.001   0.998   0.007   0.998   0.500   0.156   0.848
    // 24.0     0.095   0.30416 -0.118  0.001   0.998   0.011   0.999   0.312   0.066   1.494
    // 28.0     0.057   0.42323 -0.126  0.002   0.998   0.013   0.999   0.135   0.035   2.463

    double[] t_lift_NACA4_1005 = {-0.018, -0.024, -0.032, -0.039, -0.027, 0.111 , 0.538 , 1.188 , 1.623 , 1.402 , 0.812 , 0.363 , 0.175 , 0.095 , 0.057     };

    double[] t_drag_NACA4_1005 = { 0.36031, 0.26584, 0.37840, 0.21784, 0.12650, 0.06993, 0.04199, 0.01390, 0.02178, 0.05662, 0.10739, 0.19528, 0.34912, 0.30416, 0.42323};


    // Name = NACA 15405
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.012  0.32283 -0.134  0.887   0.006   0.962   0.019   -0.039  1.795   -10.522
    // -24.0    -0.015  0.24936 -0.131  0.864   0.005   0.965   0.016   -0.058  3.235   -8.773
    // -20.0    -0.015  0.31099 -0.128  0.834   0.005   0.968   0.013   -0.047  -0.436  -8.445
    // -16.0    -0.005  0.19555 -0.124  0.794   0.004   0.971   0.010   -0.024  0.175   -26.339
    // -12.0    0.052   0.12269 -0.123  0.742   0.003   0.974   0.008   0.425   0.229   2.600
    // -8.0     0.302   0.07011 -0.118  0.680   0.002   0.974   0.006   4.305   0.229   0.641
    // -4.0     0.914   0.04890 -0.104  0.596   0.001   0.970   0.003   18.695  0.276   0.364
    // -0.0     1.398   0.04196 -0.147  0.423   0.009   0.956   0.013   33.316  0.458   0.355
    // 4.0      2.153   0.02201 -0.362  0.402   0.117   0.934   1.000   97.804  0.152   0.418
    // 8.0      1.874   0.05406 -0.100  0.000   0.998   0.001   0.998   34.664  0.502   0.303
    // 12.0     1.117   0.09799 -0.101  0.000   0.998   0.001   0.998   11.399  0.232   0.340
    // 16.0     0.488   0.16991 -0.126  0.000   0.998   0.003   0.998   2.874   0.203   0.507
    // 20.0     0.226   0.29952 -0.143  0.000   0.998   0.004   0.998   0.755   0.147   0.882
    // 24.0     0.119   0.29247 -0.164  0.000   0.998   0.007   0.998   0.406   -0.018  1.629
    // 28.0     0.070   0.38894 -0.185  0.000   0.998   0.012   0.998   0.179   -0.179  2.909
    double[] t_lift_NACA4_1505 = {-0.012, -0.015, -0.015, -0.005, 0.052 , 0.302 , 0.914 , 1.398 , 2.153 , 1.874 , 1.117 , 0.488 , 0.226 , 0.119 , 0.070     };

    double[] t_drag_NACA4_1505 = { 0.32283, 0.24936, 0.31099, 0.19555, 0.12269, 0.07011, 0.04890, 0.04196, 0.02201, 0.05406, 0.09799, 0.16991, 0.29952, 0.29247, 0.38894};

    // Name = NACA 20405
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.008  0.57582 -0.160  0.842   0.006   0.959   0.016   -0.014  -4.904  -19.221
    // -24.0    -0.007  0.40338 -0.154  0.808   0.006   0.961   0.012   -0.018  -0.403  -21.406
    // -20.0    -0.001  0.26903 -0.155  0.769   0.005   0.962   0.011   -0.003  0.299   0.250
    // -16.0    0.024   0.17489 -0.156  0.726   0.004   0.961   0.010   0.139   0.245   6.654
    // -12.0    0.119   0.11192 -0.154  0.676   0.003   0.957   0.008   1.068   0.241   1.540
    // -8.0     0.468   0.07128 -0.152  0.618   0.002   0.949   0.006   6.571   0.232   0.574
    // -4.0     1.259   0.05833 -0.134  0.438   0.001   0.935   0.003   21.578  0.274   0.357
    // -0.0     1.803   0.04912 -0.184  0.417   0.008   0.913   0.011   36.714  0.330   0.352
    // 4.0      2.145   0.03163 -0.205  0.015   0.998   0.014   0.998   67.810  0.031   0.346
    // 8.0      2.320   0.05230 -0.071  0.000   0.188   0.000   0.999   44.360  0.385   0.280
    // 12.0     1.427   0.09462 -0.108  0.000   0.998   0.000   0.998   15.077  0.201   0.326
    // 16.0     0.621   0.15797 -0.154  0.000   0.998   0.002   0.998   3.932   0.189   0.498
    // 20.0     0.282   0.25769 -0.178  0.000   0.998   0.003   0.998   1.094   0.156   0.881
    // 24.0     0.145   0.40268 -0.199  0.000   0.998   0.004   0.998   0.360   0.007   1.620
    // 28.0     0.083   0.37534 -0.226  0.000   0.998   0.007   0.998   0.222   -0.194  2.963

    double[] t_lift_NACA4_2005 = {-0.008, -0.007, -0.001, 0.024 , 0.119 , 0.468 , 1.259 , 1.803 , 2.145 , 2.320 , 1.427 , 0.621 , 0.282 , 0.145 , 0.083     };


    double[] t_drag_NACA4_2005 = {0.57582, 0.40338, 0.26903, 0.17489, 0.11192, 0.07128, 0.05833, 0.04912, 0.03163, 0.05230, 0.09462, 0.15797, 0.25769, 0.40268, 0.37534};

    // Name = NACA 0010
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.312  0.58589 0.011   1.000   0.002   1.000   0.010   -0.532  0.239   0.286
    // -24.0    -0.442  0.39243 0.010   1.000   0.002   1.000   0.009   -1.126  0.239   0.272
    // -20.0    -0.626  0.24464 0.008   1.000   0.003   1.000   0.006   -2.561  0.242   0.262
    // -16.0    -0.831  0.15129 0.007   1.000   0.004   1.000   0.007   -5.494  0.241   0.258
    // -12.0    -0.877  0.09026 0.005   0.980   0.005   1.000   0.010   -9.720  0.313   0.256
    // -8.0     -0.873  0.01804 0.009   0.953   0.007   1.000   0.972   -48.387 0.252   0.261
    // -4.0     -0.470  0.01248 0.005   0.887   0.157   1.000   0.999   -37.657 0.261   0.260
    // -0.0     0.000   0.00939 -0.000  0.683   0.683   1.000   0.999   0.000   0.260   0.250
    // 4.0      0.470   0.01243 -0.005  0.157   0.887   1.000   0.999   37.800  0.261   0.260
    // 8.0      0.873   0.01805 -0.009  0.007   0.953   0.971   0.999   48.340  0.252   0.261
    // 12.0     0.877   0.09026 -0.005  0.005   0.980   0.010   0.998   9.720   0.313   0.256
    // 16.0     0.831   0.15130 -0.007  0.004   0.998   0.007   0.998   5.494   0.241   0.258
    // 20.0     0.626   0.24464 -0.008  0.003   0.998   0.006   0.998   2.561   0.242   0.262
    // 24.0     0.442   0.39243 -0.010  0.002   0.998   0.009   0.999   1.126   0.239   0.272
    // 28.0     0.312   0.58589 -0.011  0.002   0.998   0.010   0.999   0.532   0.239   0.286

    double[] t_lift_NACA4_0010 = {-0.312, -0.442, -0.626, -0.831, -0.877, -0.873, -0.470, 0.000 , 0.470 , 0.873 , 0.877 , 0.831 , 0.626 , 0.442 , 0.312     };


    double[] t_drag_NACA4_0010 = {0.58589, 0.39243, 0.24464, 0.15129, 0.09026, 0.01804, 0.01248, 0.00939, 0.01243, 0.01805, 0.09026, 0.15130, 0.24464, 0.39243, 0.58589};

    // Name = NACA 5410
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.225  0.57593 -0.043  0.968   0.002   0.988   0.012   -0.391  0.269   0.060
    // -24.0    -0.302  0.43337 -0.041  0.961   0.002   0.986   0.010   -0.698  0.256   0.113
    // -20.0    -0.403  0.26078 -0.042  0.945   0.003   0.988   0.010   -1.544  0.251   0.146
    // -16.0    -0.493  0.16004 -0.041  0.922   0.005   1.000   0.008   -3.079  0.216   0.166
    // -12.0    -0.469  0.09461 -0.044  0.882   0.006   1.000   0.010   -4.962  0.280   0.156
    // -8.0     -0.230  0.05678 -0.049  0.818   0.007   1.000   0.015   -4.045  0.369   0.036
    // -4.0     0.159   0.01223 -0.119  0.716   0.019   1.000   0.999   12.979  0.337   1.000
    // -0.0     0.635   0.01243 -0.124  0.507   0.234   1.000   0.999   51.108  0.262   0.446
    // 4.0      1.103   0.01368 -0.130  0.382   0.998   1.000   0.998   80.657  0.262   0.368
    // 8.0      1.474   0.02429 -0.135  0.012   0.998   0.928   0.998   60.678  -0.045  0.341
    // 12.0     1.325   0.08785 -0.065  0.003   0.999   0.019   0.999   15.084  0.552   0.299
    // 16.0     1.202   0.14406 -0.053  0.002   0.998   0.005   0.998   8.346   0.277   0.294
    // 20.0     0.891   0.23484 -0.053  0.002   0.998   0.004   0.998   3.793   0.246   0.310
    // 24.0     0.603   0.36941 -0.055  0.001   0.999   0.004   0.999   1.632   0.229   0.342
    // 28.0     0.409   0.55037 -0.063  0.001   0.999   0.007   0.999   0.743   0.209   0.405

    double[] t_lift_NACA4_0510 = {-0.225, -0.302, -0.403, -0.493, -0.469, -0.230, 0.159 , 0.635 , 1.103 , 1.474 , 1.325 , 1.202 , 0.891 , 0.603 , 0.409     };



    double[] t_drag_NACA4_0510 = {0.57593, 0.43337, 0.26078, 0.16004, 0.09461, 0.05678, 0.01223, 0.01243, 0.01368, 0.02429, 0.08785, 0.14406, 0.23484, 0.36941, 0.55037};

    // Name = NACA10410
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.156  0.55779 -0.092  0.927   0.003   0.978   0.014   -0.280  0.347   -0.342
    // -24.0    -0.194  0.42638 -0.089  0.909   0.002   0.978   0.011   -0.456  0.364   -0.206
    // -20.0    -0.231  0.27471 -0.084  0.880   0.002   0.979   0.008   -0.840  0.281   -0.113
    // -16.0    -0.228  0.17306 -0.088  0.841   0.003   0.982   0.010   -1.317  0.277   -0.134
    // -12.0    -0.098  0.09841 -0.087  0.785   0.007   0.985   0.009   -1.001  0.266   -0.637
    // -8.0     0.216   0.05836 -0.095  0.716   0.008   0.984   0.012   3.705   0.291   0.688
    // -4.0     0.617   0.03798 -0.117  0.616   0.012   0.983   0.030   16.237  0.394   0.440
    // -0.0     1.250   0.01607 -0.244  0.429   0.051   0.972   0.999   77.811  0.375   0.445
    // 4.0      1.696   0.01798 -0.252  0.396   0.998   0.950   0.998   94.302  0.269   0.399
    // 8.0      2.078   0.02261 -0.260  0.357   0.998   0.913   0.999   91.919  0.274   0.375
    // 12.0     2.136   0.04955 -0.263  0.001   0.998   0.786   0.999   43.120  0.599   0.373
    // 16.0     1.593   0.13978 -0.091  0.000   0.998   0.003   0.999   11.398  0.433   0.307
    // 20.0     1.162   0.22691 -0.084  -0.000  0.998   0.002   0.998   5.123   0.253   0.322
    // 24.0     0.764   0.34279 -0.088  -0.001  0.998   0.002   0.998   2.228   0.219   0.365
    // 28.0     0.502   0.50645 -0.105  -0.000  0.998   0.004   0.998   0.992   0.186   0.459

    double[] t_lift_NACA4_1010 = {-0.156, -0.194, -0.231, -0.228, -0.098, 0.216 , 0.617 , 1.250 , 1.696 , 2.078 , 2.136 , 1.593 , 1.162 , 0.764 , 0.502     };

    double[] t_drag_NACA4_1010 = {0.55779, 0.42638, 0.27471, 0.17306, 0.09841, 0.05836, 0.03798, 0.01607, 0.01798, 0.02261, 0.04955, 0.13978, 0.22691, 0.34279, 0.50645};

    // Name = NACA 15410
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.104  0.56368 -0.136  0.881   0.003   0.971   0.016   -0.184  0.933   -1.064
    // -24.0    -0.114  0.38568 -0.129  0.850   0.003   0.973   0.012   -0.296  42.400  -0.882
    // -20.0    -0.104  0.26987 -0.125  0.812   0.003   0.974   0.010   -0.385  0.119   -0.950
    // -16.0    -0.027  0.17805 -0.118  0.766   0.003   0.974   0.007   -0.154  0.269   -4.044
    // -12.0    0.202   0.10790 -0.131  0.713   0.003   0.971   0.010   1.870   0.277   0.897
    // -8.0     0.620   0.06455 -0.136  0.643   0.008   0.965   0.011   9.606   0.272   0.469
    // -4.0     1.079   0.04598 -0.150  0.538   0.011   0.955   0.016   23.467  0.367   0.389
    // -0.0     1.513   0.03854 -0.240  0.419   0.029   0.934   0.141   39.263  0.438   0.409
    // 4.0      2.235   0.02495 -0.368  0.400   0.123   0.906   0.999   89.592  0.375   0.415
    // 8.0      2.634   0.03055 -0.380  0.374   0.998   0.868   0.998   86.238  0.291   0.394
    // 12.0     2.681   0.06598 -0.386  -0.000  0.998   0.759   0.998   40.632  0.680   0.394
    // 16.0     2.008   0.13522 -0.111  -0.001  0.998   0.001   0.998   14.853  0.490   0.305
    // 20.0     1.410   0.21574 -0.081  -0.002  0.998   0.000   0.998   6.533   0.268   0.307
    // 24.0     0.905   0.33285 -0.091  -0.001  0.997   0.000   0.998   2.718   0.209   0.351
    // 28.0     0.580   0.51209 -0.115  -0.001  0.997   0.001   0.997   1.133   0.176   0.448

    double[] t_lift_NACA4_1510 = {-0.104, -0.114, -0.104, -0.027, 0.202 , 0.620 , 1.079 , 1.513 , 2.235 , 2.634 , 2.681 , 2.008 , 1.410 , 0.905 , 0.580     };



    double[] t_drag_NACA4_1510 = {0.56368, 0.38568, 0.26987, 0.17805, 0.10790, 0.06455, 0.04598, 0.03854, 0.02495, 0.03055, 0.06598, 0.13522, 0.21574, 0.33285, 0.51209};

    // Name = NACA 20410
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.064  0.50315 -0.170  0.830   0.004   0.968   0.016   -0.128  -0.384  -2.397
    // -24.0    -0.054  0.36072 -0.163  0.794   0.004   0.967   0.013   -0.149  0.050   -2.791
    // -20.0    -0.008  0.25522 -0.159  0.753   0.003   0.965   0.011   -0.032  0.178   -19.222
    // -16.0    0.128   0.18811 -0.150  0.707   0.003   0.960   0.007   0.679   0.226   1.426
    // -12.0    0.448   0.11397 -0.148  0.654   0.002   0.951   0.006   3.933   0.277   0.580
    // -8.0     0.978   0.06955 -0.174  0.587   0.005   0.940   0.011   14.056  0.284   0.427
    // -4.0     1.521   0.05437 -0.185  0.436   0.010   0.919   0.013   27.966  0.332   0.371
    // -0.0     1.903   0.05042 -0.250  0.416   0.018   0.895   0.048   37.747  0.399   0.381
    // 4.0      2.706   0.03367 -0.361  0.402   0.083   0.865   0.251   80.377  0.459   0.384
    // 8.0      3.077   0.04065 -0.495  0.384   0.183   0.828   0.999   75.687  0.542   0.411
    // 12.0     3.202   0.08594 -0.506  -0.001  0.997   0.732   0.997   37.261       0.408
    // 16.0     2.347   0.13276      -0.002  0.998   -0.000  0.998   17.682        
    // 20.0     1.618   0.21740      -0.002  0.998   -0.001  0.998   7.441         
    // 24.0     1.018   0.32675      -0.001  0.998   -0.001  0.998   3.114         
    // 28.0     0.640   0.47934      -0.001  0.998   -0.002  0.998   1.336         
    
    double[] t_lift_NACA4_2010 = {-0.064, -0.054, -0.008, 0.128 , 0.448 , 0.978 , 1.521 , 1.903 , 2.706 , 3.077 , 3.202 , 2.347 , 1.618 , 1.018 , 0.640     };


    double[] t_drag_NACA4_2010 = {0.50315, 0.36072, 0.25522, 0.18811, 0.11397, 0.06955, 0.05437, 0.05042, 0.03367, 0.04065, 0.08594, 0.13276, 0.21740, 0.32675, 0.47934};
    

    // Name = NACA 0015
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.909  0.37476 0.021   1.000   0.005   1.000   0.024   -2.425  0.248   0.273
    // -24.0    -1.075  0.27558 0.020   1.000   0.006   1.000   0.038   -3.902  0.245   0.269
    // -20.0    -1.207  0.18765 0.019   0.978   0.007   1.000   0.057   -6.432  0.240   0.266
    // -16.0    -1.247  0.11477 0.019   0.961   0.011   1.000   0.135   -10.862 0.271   0.265
    // -12.0    -1.230  0.03657 0.020   0.935   0.017   1.000   0.641   -33.619 0.264   0.266
    // -8.0     -0.942  0.01998 0.014   0.879   0.038   1.000   0.938   -47.139 0.267   0.265
    // -4.0     -0.490  0.01435 0.007   0.758   0.231   1.000   0.998   -34.131 0.265   0.265
    // -0.0     0.000   0.01263 -0.000  0.504   0.504   1.000   0.998   0.000   0.265   0.250
    // 4.0      0.490   0.01434 -0.007  0.231   0.757   1.000   0.999   34.175  0.265   0.265
    // 8.0      0.942   0.01998 -0.014  0.038   0.879   0.938   0.999   47.133  0.267   0.265
    // 12.0     1.230   0.03657 -0.020  0.017   0.935   0.641   0.998   33.620  0.264   0.266
    // 16.0     1.247   0.11477 -0.019  0.011   0.961   0.135   0.999   10.862  0.271   0.265
    // 20.0     1.207   0.18765 -0.019  0.007   0.978   0.057   0.999   6.432   0.240   0.266
    // 24.0     1.075   0.27559 -0.020  0.006   0.990   0.038   0.999   3.902   0.245   0.269
    // 28.0     0.909   0.37476 -0.021  0.005   0.998   0.024   0.999   2.425   0.248   0.273

    double[] t_lift_NACA4_0015 = {-0.909, -1.075, -1.207, -1.247, -1.230, -0.942, -0.490, 0.000 , 0.490 , 0.942 , 1.230 , 1.247 , 1.207 , 1.075 , 0.909     };



    double[] t_drag_NACA4_0015 = {0.37476, 0.27558, 0.18765, 0.11477, 0.03657, 0.01998, 0.01435, 0.01263, 0.01434, 0.01998, 0.03657, 0.11477, 0.18765, 0.27559, 0.37476};

    // Name = NACA 5415
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.691  0.37662 -0.040  0.956   0.008   0.984   0.016   -1.835  0.225   0.191
    // -24.0    -0.814  0.27866 -0.044  0.940   0.008   0.984   0.021   -2.919  0.208   0.197
    // -20.0    -0.857  0.19629 -0.047  0.916   0.009   0.985   0.029   -4.365  0.685   0.195
    // -16.0    -0.790  0.12841 -0.054  0.882   0.010   0.986   0.044   -6.156  0.307   0.182
    // -12.0    -0.584  0.07936 -0.063  0.829   0.015   0.990   0.077   -7.358  0.344   0.142
    // -8.0     -0.283  0.02303 -0.101  0.751   0.024   1.000   0.622   -12.299 0.323   -0.108
    // -4.0     0.176   0.01448 -0.118  0.626   0.059   1.000   0.998   12.148  0.276   0.923
    // -0.0     0.669   0.01257 -0.126  0.433   0.239   0.983   0.999   53.266  0.266   0.438
    // 4.0      1.147   0.01587 -0.134  0.367   0.960   0.964   0.998   72.246  0.268   0.367
    // 8.0      1.583   0.02096 -0.142  0.262   0.998   0.921   0.998   75.521  0.267   0.340
    // 12.0     1.872   0.03973 -0.146  0.020   0.998   0.776   0.998   47.114  0.269   0.328
    // 16.0     1.913   0.06716 -0.148  0.009   0.998   0.603   0.998   28.482  0.279   0.327
    // 20.0     1.706   0.13226 -0.142  0.004   0.999   0.361   0.999   12.896  0.320   0.333
    // 24.0     1.374   0.26459 -0.110  0.003   0.998   0.079   0.999   5.193   0.321   0.330
    // 28.0     1.114   0.37281 -0.100  0.002   0.999   0.037   0.999   2.988   0.291   0.340

    double[] t_lift_NACA4_0515 = {-0.691, -0.814, -0.857, -0.790, -0.584, -0.283, 0.176 , 0.669 , 1.147 , 1.583 , 1.872 , 1.913 , 1.706 , 1.374 , 1.114     };


    double[] t_drag_NACA4_0515 = {0.37662, 0.27866, 0.19629, 0.12841, 0.07936, 0.02303, 0.01448, 0.01257, 0.01587, 0.02096, 0.03973, 0.06716, 0.13226, 0.26459, 0.37281};

    // Name = NACA 10415
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.488  0.39183 -0.092  0.911   0.008   0.977   0.015   -1.246  0.215   0.062
    // -24.0    -0.539  0.28429 -0.094  0.884   0.009   0.978   0.017   -1.896  0.142   0.076
    // -20.0    -0.530  0.20067 -0.097  0.849   0.009   0.979   0.019   -2.643  0.307   0.068
    // -16.0    -0.398  0.13467 -0.102  0.804   0.010   0.979   0.023   -2.958  0.291   -0.006
    // -12.0    -0.120  0.08682 -0.114  0.742   0.013   0.979   0.035   -1.380  0.294   -0.698
    // -8.0     0.256   0.05511 -0.131  0.667   0.019   0.976   0.061   4.647   0.334   0.760
    // -4.0     0.704   0.03087 -0.183  0.550   0.033   0.968   0.272   22.816  0.360   0.510
    // -0.0     1.315   0.01847 -0.247  0.420   0.091   0.949   0.999   71.164  0.320   0.438
    // 4.0      1.764   0.02077 -0.257  0.388   0.998   0.922   0.998   84.921  0.274   0.396
    // 8.0      2.195   0.02592 -0.268  0.352   0.998   0.881   0.999   84.666  0.278   0.372
    // 12.0     2.496   0.03499 -0.278  0.296   0.999   0.820   0.999   71.328  0.293   0.361
    // 16.0     2.488   0.07778 -0.281  0.006   0.999   0.672   0.999   31.992  0.220   0.363
    // 20.0     2.241   0.11730 -0.285  0.001   0.999   0.573   0.999   19.102  0.243   0.377
    // 24.0     1.841   0.18043 -0.285  -0.001  0.999   0.453   0.999   10.202  0.311   0.405
    // 28.0     1.368   0.34584 -0.232  -0.001  0.999   0.129   1.000   3.956   0.363   0.420

    double[] t_lift_NACA4_1015 = {-0.488, -0.539, -0.530, -0.398, -0.120, 0.256 , 0.704 , 1.315 , 1.764 , 2.195 , 2.496 , 2.488 , 2.241 , 1.841 , 1.368     };


    double[] t_drag_NACA4_1015 = {0.39183, 0.28429, 0.20067, 0.13467, 0.08682, 0.05511, 0.03087, 0.01847, 0.02077, 0.02592, 0.03499, 0.07778, 0.11730, 0.18043, 0.34584};

    // Name = NACA 15415
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.329  0.40223 -0.142  0.861   0.005   0.973   0.019   -0.819  0.210   -0.181
    // -24.0    -0.323  0.29105 -0.142  0.827   0.006   0.972   0.019   -1.109  0.177   -0.189
    // -20.0    -0.244  0.20322 -0.136  0.785   0.010   0.971   0.015   -1.199  0.248   -0.307
    // -16.0    -0.040  0.13660 -0.141  0.736   0.011   0.968   0.017   -0.291  0.276   -3.298
    // -12.0    0.306   0.09048 -0.150  0.679   0.012   0.963   0.021   3.379   0.285   0.740
    // -8.0     0.727   0.06098 -0.168  0.609   0.015   0.953   0.032   11.921  0.307   0.481
    // -4.0     1.166   0.04689 -0.199  0.444   0.026   0.937   0.063   24.879  0.369   0.421
    // -0.0     1.635   0.03655 -0.276  0.417   0.055   0.913   0.266   44.728  0.402   0.419
    // 4.0      2.322   0.02820 -0.374  0.398   0.140   0.883   0.999   82.358  0.354   0.411
    // 8.0      2.726   0.03441 -0.390  0.370   0.998   0.844   0.998   79.228  0.290   0.393
    // 12.0     3.073   0.04338 -0.404  0.350   0.998   0.791   0.998   70.832  0.324   0.382
    // 16.0     3.029   0.09726 -0.412  0.002   0.998   0.679   0.999   31.139  0.193   0.386
    // 20.0     2.721   0.13509 -0.424  -0.003  0.998   0.616   0.999   20.142  0.221   0.406
    // 24.0     2.243   0.18511 -0.435  -0.003  0.998   0.555   0.998   12.118  0.231   0.444
    // 28.0     1.750   0.25303 -0.442  -0.003  0.999   0.483   0.999   6.916   0.236   0.503

    double[] t_lift_NACA4_1515 = {-0.329, -0.323, -0.244, -0.040, 0.306 , 0.727 , 1.166 , 1.635 , 2.322 , 2.726 , 3.073 , 3.029 , 2.721 , 2.243 , 1.750     };


    double[] t_drag_NACA4_1515 = {0.40223, 0.29105, 0.20322, 0.13660, 0.09048, 0.06098, 0.04689, 0.03655, 0.02820, 0.03441, 0.04338, 0.09726, 0.13509, 0.18511, 0.25303};

    // Name = NACA 20415
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.204  0.41542 -0.172  0.815   0.004   0.967   0.016   -0.492  0.296   -0.594
    // -24.0    -0.151  0.29248 -0.175  0.775   0.005   0.965   0.018   -0.518  0.284   -0.905
    // -20.0    -0.011  0.21007 -0.179  0.732   0.005   0.961   0.019   -0.053  0.265   -15.854
    // -16.0    0.266   0.14033 -0.181  0.684   0.009   0.953   0.018   1.899   0.258   0.930
    // -12.0    0.687   0.09525 -0.184  0.631   0.012   0.942   0.018   7.213   0.270   0.518
    // -8.0     1.170   0.06669 -0.199  0.565   0.014   0.926   0.022   17.548  0.301   0.420
    // -4.0     1.610   0.05493 -0.231  0.434   0.018   0.903   0.038   29.306  0.367   0.394
    // -0.0     2.020   0.05076 -0.298  0.415   0.038   0.878   0.110   39.797  0.369   0.398
    // 4.0      2.809   0.03721 -0.374  0.401   0.099   0.848   0.272   75.474  0.430   0.383
    // 8.0      3.182   0.04496 -0.507  0.383   0.190   0.812   0.999   70.765  0.472   0.409
    // 12.0     3.506   0.05533 -0.529  0.364   0.997   0.769   0.997   63.361  0.351   0.401
    // 16.0     3.536   0.12258 -0.543  -0.005  0.998   0.672   0.998   28.850  0.146   0.403
    // 20.0     3.177   0.16124 -0.563  -0.005  0.998   0.626   0.998   19.702  0.206   0.427
    // 24.0     2.624   0.21018 -0.583  -0.005  0.998   0.583   0.998   12.484  0.217   0.472
    // 28.0     2.053   0.26914 -0.600  -0.005  0.997   0.540   0.998   7.628   0.219   0.542

    double[] t_lift_NACA4_2015 = {-0.204, -0.151, -0.011, 0.266 , 0.687 , 1.170 , 1.610 , 2.020 , 2.809 , 3.182 , 3.506 , 3.536 , 3.177 , 2.624 , 2.053     };


    double[] t_drag_NACA4_2015 = {0.41542, 0.29248, 0.21007, 0.14033, 0.09525, 0.06669, 0.05493, 0.05076, 0.03721, 0.04496, 0.05533, 0.12258, 0.16124, 0.21018, 0.26914};

    // Name = NACA 0020
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -1.475  0.31137 0.040   0.978   0.013   1.000   0.103   -4.736  0.242   0.277
    // -24.0    -1.605  0.21713 0.039   0.966   0.015   1.000   0.156   -7.390  0.237   0.274
    // -20.0    -1.658  0.13342 0.038   0.949   0.018   1.000   0.269   -12.425 0.414   0.273
    // -16.0    -1.578  0.06898 0.035   0.921   0.027   1.000   0.467   -22.871 0.279   0.272
    // -12.0    -1.353  0.03465 0.029   0.876   0.045   1.000   0.714   -39.052 0.274   0.272
    // -8.0     -0.979  0.02160 0.021   0.789   0.108   1.000   0.901   -45.316 0.272   0.271
    // -4.0     -0.505  0.01676 0.011   0.622   0.247   1.000   0.969   -30.149 0.271   0.271
    // -0.0     -0.000  0.01576 -0.000  0.421   0.421   1.000   0.999   -0.000  0.271   0.250
    // 4.0      0.505   0.01676 -0.011  0.247   0.622   0.969   0.999   30.149  0.271   0.271
    // 8.0      0.979   0.02160 -0.021  0.108   0.789   0.901   0.999   45.316  0.272   0.271
    // 12.0     1.353   0.03465 -0.029  0.045   0.876   0.714   0.998   39.052  0.274   0.272
    // 16.0     1.578   0.06898 -0.035  0.027   0.921   0.467   0.999   22.871  0.279   0.272
    // 20.0     1.658   0.13342 -0.038  0.018   0.949   0.269   0.999   12.425  0.414   0.273
    // 24.0     1.605   0.21713 -0.039  0.015   0.966   0.156   1.000   7.390   0.237   0.274
    // 28.0     1.475   0.31137 -0.040  0.013   0.978   0.103   0.999   4.736   0.242   0.277

    double[] t_lift_NACA4_0020 = {-1.475, -1.605, -1.658, -1.578, -1.353, -0.979, -0.505, -0.000, 0.505 , 0.979 , 1.353 , 1.578 , 1.658 , 1.605 , 1.475     };

    double[] t_drag_NACA4_0020 = {0.31137, 0.21713, 0.13342, 0.06898, 0.03465, 0.02160, 0.01676, 0.01576, 0.01676, 0.02160, 0.03465, 0.06898, 0.13342, 0.21713, 0.31137};


    // Name = NACA 5420
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -1.172  0.32119 -0.046  0.937   0.013   0.980   0.064   -3.650  0.155   0.211
    // -24.0    -1.203  0.23686 -0.049  0.914   0.015   0.981   0.077   -5.080  0.484   0.210
    // -20.0    -1.140  0.16504 -0.053  0.883   0.020   0.981   0.095   -6.909  0.310   0.203
    // -16.0    -0.964  0.10403 -0.063  0.840   0.024   0.982   0.160   -9.270  0.302   0.185
    // -12.0    -0.674  0.05455 -0.077  0.774   0.034   0.983   0.311   -12.350 0.313   0.135
    // -8.0     -0.307  0.01755 -0.104  0.682   0.055   0.983   0.908   -17.486 0.295   -0.090
    // -4.0     0.193   0.01673 -0.116  0.547   0.113   0.978   0.998   11.558  0.272   0.852
    // -0.0     0.704   0.01727 -0.127  0.417   0.251   0.962   1.000   40.790  0.272   0.431
    // 4.0      1.190   0.01858 -0.138  0.357   0.835   0.934   1.000   64.050  0.273   0.366
    // 8.0      1.645   0.02406 -0.149  0.267   0.938   0.883   1.000   68.400  0.274   0.341
    // 12.0     2.007   0.03759 -0.158  0.123   0.972   0.782   1.000   53.405  0.276   0.329
    // 16.0     2.207   0.06538 -0.164  0.031   0.999   0.635   1.000   33.754  0.291   0.324
    // 20.0     2.249   0.10615 -0.168  0.017   1.000   0.505   1.000   21.185  0.167   0.325
    // 24.0     2.126   0.16852 -0.171  0.010   1.000   0.392   1.000   12.616  0.252   0.330
    // 28.0     1.864   0.26323 -0.168  0.007   1.000   0.269   1.000   7.081   0.261   0.340

    double[] t_lift_NACA4_0520 = {-1.172, -1.203, -1.140, -0.964, -0.674, -0.307, 0.193 , 0.704 , 1.190 , 1.645 , 2.007 , 2.207 , 2.249 , 2.126 , 1.864     };


    double[] t_drag_NACA4_0520 = {0.32119, 0.23686, 0.16504, 0.10403, 0.05455, 0.01755, 0.01673, 0.01727, 0.01858, 0.02406, 0.03759, 0.06538, 0.10615, 0.16852, 0.26323};

    // Name = NACA 10420
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.889  0.32829 -0.108  0.889   0.013   0.974   0.040   -2.707  0.347   0.129
    // -24.0    -0.860  0.24399 -0.110  0.857   0.014   0.974   0.046   -3.526  0.304   0.122
    // -20.0    -0.721  0.17311 -0.117  0.817   0.016   0.975   0.059   -4.163  0.287   0.088
    // -16.0    -0.470  0.11761 -0.125  0.767   0.020   0.973   0.074   -3.997  0.282   -0.015
    // -12.0    -0.120  0.07725 -0.136  0.703   0.028   0.970   0.097   -1.558  0.301   -0.878
    // -8.0     0.298   0.04607 -0.164  0.620   0.038   0.963   0.201   6.472   0.351   0.799
    // -4.0     0.875   0.01875 -0.237  0.475   0.065   0.950   0.999   46.674  0.329   0.520
    // -0.0     1.381   0.02113 -0.249  0.415   0.134   0.927   0.999   65.347  0.277   0.430
    // 4.0      1.832   0.02459 -0.262  0.384   0.294   0.896   0.999   74.517  0.281   0.393
    // 8.0      2.269   0.02947 -0.276  0.348   0.999   0.851   0.999   76.997  0.284   0.372
    // 12.0     2.641   0.03927 -0.290  0.294   0.999   0.795   0.999   67.247  0.295   0.360
    // 16.0     2.841   0.05832 -0.302  0.202   0.999   0.720   0.999   48.709  0.351   0.356
    // 20.0     2.817   0.11372 -0.308  0.015   0.999   0.592   0.999   24.771  0.180   0.359
    // 24.0     2.639   0.16530 -0.316  0.004   0.999   0.513   0.999   15.964  0.220   0.370
    // 28.0     2.299   0.23083 -0.323  0.001   0.999   0.444   0.999   9.958   0.229   0.391

    double[] t_lift_NACA4_1020 = {-0.889, -0.860, -0.721, -0.470, -0.120, 0.298 , 0.875 , 1.381 , 1.832 , 2.269 , 2.641 , 2.841 , 2.817 , 2.639 , 2.299     };


    double[] t_drag_NACA4_1020 = {0.32829, 0.24399, 0.17311, 0.11761, 0.07725, 0.04607, 0.01875, 0.02113, 0.02459, 0.02947, 0.03927, 0.05832, 0.11372, 0.16530, 0.23083};

    // Name = NACA 15420
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.610  0.33188 -0.148  0.843   0.014   0.969   0.025   -1.839  0.277   0.008
    // -24.0    -0.529  0.24819 -0.150  0.807   0.014   0.967   0.028   -2.132  0.284   -0.034
    // -20.0    -0.341  0.17883 -0.157  0.764   0.015   0.965   0.034   -1.905  0.289   -0.211
    // -16.0    -0.031  0.12377 -0.169  0.712   0.017   0.960   0.046   -0.253  0.287   -5.163
    // -12.0    0.365   0.08391 -0.183  0.651   0.021   0.952   0.060   4.344   0.288   0.753
    // -8.0     0.806   0.05803 -0.201  0.574   0.031   0.940   0.080   13.885  0.315   0.500
    // -4.0     1.260   0.04408 -0.242  0.439   0.045   0.920   0.163   28.577  0.385   0.442
    // -0.0     2.007   0.02681 -0.363  0.415   0.085   0.894   0.999   74.844  0.371   0.431
    // 4.0      2.411   0.03169 -0.381  0.398   0.166   0.862   0.999   76.096  0.295   0.408
    // 8.0      2.820   0.03844 -0.400  0.368   0.999   0.823   0.999   73.363  0.299   0.392
    // 12.0     3.181   0.04803 -0.419  0.346   0.999   0.774   0.999   66.235  0.311   0.382
    // 16.0     3.429   0.06302 -0.437  0.312   0.999   0.720   0.999   54.420  0.407   0.377
    // 20.0     3.413   0.08593 -0.455  0.267   0.999   0.665   0.999   39.716  0.163   0.383
    // 24.0     3.117   0.18356 -0.464  -0.002  0.999   0.563   0.999   16.979  0.216   0.399
    // 28.0     2.695   0.24323 -0.479  -0.005  0.999   0.513   0.999   11.079  0.215   0.428

    double[] t_lift_NACA4_1520 = {-0.610, -0.529, -0.341, -0.031, 0.365 , 0.806 , 1.260 , 2.007 , 2.411 , 2.820 , 3.181 , 3.429 , 3.413 , 3.117 , 2.695     };


    double[] t_drag_NACA4_1520 = {0.33188, 0.24819, 0.17883, 0.12377, 0.08391, 0.05803, 0.04408, 0.02681, 0.03169, 0.03844, 0.04803, 0.06302, 0.08593, 0.18356, 0.24323};


    // Name = NACA 20420
    // Mach = 0; Re = 300000; T.U. = 1.0; T.L. = 1.0
    // Surface Finish = 0; Stall model = 0; Transition model = 1; Aspect Ratio = 0; ground effect = 0
    //       Cl      Cd      Cm 0.25 T.U.    T.L.    S.U.    S.L.    L/D     A.C.    C.P.
    // [ ]   [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]     [-]
    // -28.0    -0.384  0.33708 -0.193  0.796   0.009   0.965   0.029   -1.139  0.185   -0.253
    // -24.0    -0.247  0.24958 -0.184  0.758   0.013   0.960   0.024   -0.988  0.233   -0.497
    // -20.0    0.002   0.18194 -0.186  0.715   0.015   0.952   0.024   0.010   0.269   100.285
    // -16.0    0.368   0.12932 -0.196  0.668   0.017   0.944   0.028   2.849   0.282   0.781
    // -12.0    0.812   0.09027 -0.212  0.612   0.018   0.930   0.036   8.994   0.296   0.511
    // -8.0     1.281   0.06551 -0.238  0.534   0.022   0.914   0.053   19.551  0.311   0.435
    // -4.0     1.705   0.05543 -0.267  0.433   0.036   0.890   0.076   30.768  0.354   0.406
    // -0.0     2.139   0.05160 -0.327  0.415   0.063   0.863   0.166   41.447  0.354   0.403
    // 4.0      2.914   0.04079 -0.393  0.402   0.120   0.833   0.321   71.432  0.417   0.385
    // 8.0      3.290   0.04952 -0.519  0.383   0.205   0.798   0.999   66.443  0.468   0.408
    // 12.0     3.616   0.06034 -0.546  0.363   0.998   0.758   0.999   59.932  0.337   0.401
    // 16.0     3.894   0.07538 -0.571  0.347   0.999   0.708   0.999   51.655  0.412   0.397
    // 20.0     3.920   0.09570 -0.595  0.333   0.999   0.649   0.999   40.963  0.120   0.402
    // 24.0     3.558   0.21309 -0.615  -0.007  0.999   0.584   0.999   16.695  0.199   0.423
    // 28.0     3.063   0.26953 -0.639  -0.009  0.999   0.547   0.999   11.365  0.202   0.459

    double[] t_lift_NACA4_2020 = {-0.384, -0.247, 0.002 , 0.368 , 0.812 , 1.281 , 1.705 , 2.139 , 2.914 , 3.290 , 3.616 , 3.894 , 3.920 , 3.558 , 3.063     };

    double[] t_drag_NACA4_2020 = {0.33708, 0.24958, 0.18194, 0.12932, 0.09027, 0.06551, 0.05543, 0.05160, 0.04079, 0.04952, 0.06034, 0.07538, 0.09570, 0.21309, 0.26953};

    double[][] t_lift_NACA4 = {
      t_lift_NACA4_0005, 
      t_lift_NACA4_0505, 
      t_lift_NACA4_1005, 
      t_lift_NACA4_1505, 
      t_lift_NACA4_2005, 
      t_lift_NACA4_0010,
      t_lift_NACA4_0510,
      t_lift_NACA4_1010,
      t_lift_NACA4_1510,
      t_lift_NACA4_2010,
      t_lift_NACA4_0015,
      t_lift_NACA4_0515,
      t_lift_NACA4_1015,
      t_lift_NACA4_1515,
      t_lift_NACA4_2015,
      t_lift_NACA4_0020,
      t_lift_NACA4_0520,
      t_lift_NACA4_1020,
      t_lift_NACA4_1520,
      t_lift_NACA4_2020
    };

    double[][] t_drag_NACA4 = {
      t_drag_NACA4_0005, 
      t_drag_NACA4_0505, 
      t_drag_NACA4_1005, 
      t_drag_NACA4_1505, 
      t_drag_NACA4_2005, 

      t_drag_NACA4_0010,
      t_drag_NACA4_0510,
      t_drag_NACA4_1010,
      t_drag_NACA4_1510,
      t_drag_NACA4_2010,

      t_drag_NACA4_0015,
      t_drag_NACA4_0515,
      t_drag_NACA4_1015,
      t_drag_NACA4_1515,
      t_drag_NACA4_2015,

      t_drag_NACA4_0020,
      t_drag_NACA4_0520,
      t_drag_NACA4_1020,
      t_drag_NACA4_1520,
      t_drag_NACA4_2020
    };


    // NACA 6 series variants
    // use this to convert excel column of 25 values to array initializer:
    /* 
       (fset 'conv_25_rows_to_array_initializer
       [?\C-y ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\C-a ?\{ ?\C-e ?\} ?\C-  ?\C-\M-b ?\C-e ?\C-  ?\C-a ?\C-x ?n ?n ?\C-a ?\M-x ?r ?e ?p ?l ?  ?s ?t ?r ?i ?n ?g return ?  return ?, ?  return ?\C-b ?\C-b ?\C-d ?\C-d ?\C-a ?\C-x ?n ?w])
    */

    // NACA 64_814 with TE gap = 0.5%
    double[] t_lift_NACA_64_814_teg05 = {-0.436, -0.495, -0.549, -0.585, -0.585, -0.521, -0.415, -0.271, -0.097, 0.112, 0.353, 0.596, 0.835, 1.073, 1.302, 1.494, 1.644, 1.715, 1.408, 1.315, 1.167, 1.003, 0.843, 0.702, 0.584};
    double[] t_drag_NACA_64_814_teg05 = {0.31324, 0.2687, 0.21301, 0.17075, 0.13549, 0.1072, 0.08288, 0.06413, 0.05002, 0.01114, 0.00998, 0.00805, 0.00833, 0.00811, 0.00777, 0.021, 0.02575, 0.03206, 0.08869, 0.11247, 0.14528, 0.18438, 0.23803, 0.29672, 0.34426};
    

    // cubic-interpolate using array of -28 to +28 step 4 lift or drag data.
    // for CI, the array must have 2 padding values on each size of target range.
    // hence target range here is -20...20
    // example: ci15(t_drag_NACA4_20020, 3.21);

    // To convert Javafoil data to an array of 15 Cd values, set Polar tab to generate
    // 15 AoA values, chose single Re and press Copy(Text), and
    // then use this emacs keyboard macro (eval and then M-x execute-kbd-macro)
    /*
      (fset 'insert_javafoil_drag_15rows_test
      [?\C-y ?\C-  ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-x ?r ?t ?  ?  ?  ?  ?/ ?/ ?  return ?\C-r ?c ?d ?\C-n ?\C-n ?\C-  ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-\M-f ?\C-\M-f ?\C-x ?r ?\M-w ?\C-n ?\C-n ?  ?  ?  ?  ?d ?o ?u ?b ?l ?e ?  ?\C-b ?\[ ?\] ?\C-e ?t ?_ ?x ?y ?z ?_ ?\C-r ?n ?a ?m ?e ?\C-\M-f ?\C-f ?\C-f ?\C-f ?\C-  ?\C-\M-f ?\M-w ?\C-s ?x ?y ?z ?_ ?\C-b ?\C-f ?\C-y ?  ?= ?  ?\{ return return return return return return return return return return return return return return return ?\} ?\; ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-x ?r ?y ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-  ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-x ?r ?t ?, return ?\C-n ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\C-e return return])

      (fset 'insert_javafoil_lift_15rows_test
      [?\C-r ?c ?l ?\C-n ?\C-n ?\C-  ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-\M-f ?\C-\M-f ?\C-f ?\C-x ?r ?\M-w ?\C-n ?\C-n ?  ?  ?  ?  ?d ?o ?u ?b ?l ?e ?  ?\C-b ?\[ ?\] ?\C-e ?t ?_ ?x ?y ?z ?_ ?\C-r ?n ?a ?m ?e ?\C-\M-f ?\C-f ?\C-f ?\C-f ?\C-  ?\C-e ?\M-w ?\C-s ?x ?y ?z ?_ ?\C-b ?\C-f ?\C-y ?  ?= ?  ?\{ ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\C-m ?\} ?\; ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-x ?r ?y ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-p ?\C-e ?\C-  ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-n ?\C-x ?r ?t ?, ?\C-m ?\C-n ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\C-e ?\C-m ?\C-m])

      also see M-x edit-named-kbd-macro

    */
    double ci15(double[] tab, double aoa) {
      if (tab.length != 15) 
        System.out.println(" *** WARNING  ci15 cubic interpolates arrays of 15. got tab.length: " + tab.length);
      int aoa_4 = (int)Math.floor(aoa/4);
      int t_idx = aoa_4 + 7; // 7 is (15-1)/2

      // direct hit?
      if (aoa/4 == (double)aoa_4) // this also allows to grab any of 15 valies, -28...28: ci15(mytab, -28)
        return tab[t_idx];
      else { // rounding???
      if (t_idx < 1 || t_idx > 13) {
        System.out.println(" *** ERROR ci15 cubic interpolates arrays of 15. got bad t_idx=" + t_idx + " for aoa=" + aoa + " aoa_4=" + (double)aoa_4 + " aoa/4=" + aoa/4);
        new Exception("=================================").printStackTrace(System.out);
        System.exit(0);
      }

      }

      double c_0 = tab[t_idx-1];
      double c_1 = tab[t_idx];
      double c_2 = tab[t_idx+1];
      double c_3 = tab[t_idx+2];
      double mu = Math.abs(aoa - 4*aoa_4)/4.0;
      double res = CubicInterpolate(c_0, c_1, c_2, c_3, mu);
      return res;
    }

    // cubic-interpolate using array of -24 to +24 step 2 lift or drag data.
    // for CI, the array must have 2 padding values on each size of target range.
    // hence target range here is -20...20
    // example: ci25(t_drag_NACA_64_814_teg05, 3.21);
    //
    //
    /* 
       (fset 'conv_25_rows_to_array_initializer
       [?\C-y ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\M-^ ?\C-a ?\{ ?\C-e ?\} ?\C-  ?\C-\M-b ?\C-e ?\C-  ?\C-a ?\C-x ?n ?n ?\C-a ?\M-x ?r ?e ?p ?l ?  ?s ?t ?r ?i ?n ?g return ?  return ?, ?  return ?\C-b ?\C-b ?\C-d ?\C-d ?\C-a ?\C-x ?n ?w])
    */
    double ci25 (double[] tab, double aoa) {
      if (tab.length != 25) 
        System.out.println(" *** WARNING  ci25 cubic interpolates arrays of 25. got tab.length: " + tab.length);
      int aoa_2 = (int)Math.floor(aoa/2);
      int t_idx = aoa_2 + 12; // (25-1)/2

      // direct hit?
      if (aoa/2 == (double)aoa_2) // this also allows to grab any of 25 valies: ci25(mytab, -24)
        return tab[t_idx];

      double c_0 = tab[t_idx-1];
      double c_1 = tab[t_idx];
      double c_2 = tab[t_idx+1];
      double c_3 = tab[t_idx+2];
      double mu = Math.abs(aoa - 2*aoa_2)/2.0;
      double res = CubicInterpolate(c_0, c_1, c_2, c_3, mu);
      return res;
    }



    double ci15_from_javafoil_data (double[][] t_thick_camb, double aoa, double thickness, double camber) {

      double c_Cam0Thk5, c_Cam5Thk5, c_Cam10Thk5, c_Cam15Thk5, c_Cam20Thk5;
      double c_Cam0Thk10, c_Cam5Thk10, c_Cam10Thk10, c_Cam15Thk10, c_Cam20Thk10;
      double c_Cam0Thk15, c_Cam5Thk15, c_Cam10Thk15, c_Cam15Thk15, c_Cam20Thk15;
      double c_Cam0Thk20, c_Cam5Thk20, c_Cam10Thk20, c_Cam15Thk20, c_Cam20Thk20;
      double c_Thk5, c_Thk10, c_Thk15, c_Thk20;

      double c = 1;

      // warning: the following impies ranges. 
      if (thickness < 1) {
        System.out.println("-- warning: thickness=" + thickness + "% is less than required by  ci15_from_javafoil_data, assuming it is 1%" );
        thickness = 1;
      } else if (thickness > 20) {
        System.out.println("-- warning: thickness=" + thickness + "% is greater than required by ci15_from_javafoil_data, assuming it is 20%" );
        thickness = 20;
      }
      if (camber < -20) {
        System.out.println("-- warning: camber=" + camber + "% is less than required by ci15_from_javafoil_data, assuming it is -20%" );
        camber = -20;
      } else if (camber > 20) {
        System.out.println("-- warning: camber=" + camber + "% is greater than required by ci15_from_javafoil_data, assuming it is 20%" );
        camber = 20;
      }

      // System.out.println("-- doing C__COMP_NACA4SERIES ");
      // thickness 5%
      c_Cam0Thk5  = ci15(t_thick_camb[0], aoa);
      c_Cam5Thk5  = ci15(t_thick_camb[1], aoa);
      c_Cam10Thk5 = ci15(t_thick_camb[2], aoa);
      c_Cam15Thk5 = ci15(t_thick_camb[3], aoa);
      c_Cam20Thk5 = ci15(t_thick_camb[4], aoa);

      // thickness 10%
      c_Cam0Thk10  = ci15(t_thick_camb[5], aoa);
      c_Cam5Thk10  = ci15(t_thick_camb[6], aoa);
      c_Cam10Thk10 = ci15(t_thick_camb[7], aoa);
      c_Cam15Thk10 = ci15(t_thick_camb[8], aoa);
      c_Cam20Thk10 = ci15(t_thick_camb[9], aoa);

      // thickness 15%
      c_Cam0Thk15  = ci15(t_thick_camb[10], aoa);
      c_Cam5Thk15  = ci15(t_thick_camb[11], aoa);
      c_Cam10Thk15 = ci15(t_thick_camb[12], aoa);
      c_Cam15Thk15 = ci15(t_thick_camb[13], aoa);
      c_Cam20Thk15 = ci15(t_thick_camb[14], aoa);

      // thickness 20%
      c_Cam0Thk20  = ci15(t_thick_camb[15], aoa);
      c_Cam5Thk20  = ci15(t_thick_camb[16], aoa);
      c_Cam10Thk20 = ci15(t_thick_camb[17], aoa);
      c_Cam15Thk20 = ci15(t_thick_camb[18], aoa);
      c_Cam20Thk20 = ci15(t_thick_camb[19], aoa);

      if (-20.0 <= camber && camber < -15.0)
        {
          c_Thk5 = (camber/5 + 4)*(c_Cam15Thk5 - c_Cam20Thk5) + c_Cam20Thk5;
          c_Thk10 = (camber/5 + 4)*(c_Cam15Thk10 - c_Cam20Thk10) + c_Cam20Thk10;
          c_Thk15 = (camber/5 + 4)*(c_Cam15Thk15 - c_Cam20Thk15) + c_Cam20Thk15;
          c_Thk20 = (camber/5 + 4)*(c_Cam15Thk20 - c_Cam20Thk20) + c_Cam20Thk20;
                    
          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }
      else if (-15.0 <= camber && camber < -10.0)
        {
          c_Thk5 = (camber/5 + 3)*(c_Cam10Thk5 - c_Cam15Thk5) + c_Cam15Thk5;
          c_Thk10 = (camber/5 + 3)*(c_Cam10Thk10 - c_Cam15Thk10) + c_Cam15Thk10;
          c_Thk15 = (camber/5 + 3)*(c_Cam10Thk15 - c_Cam15Thk15) + c_Cam15Thk15;
          c_Thk20 = (camber/5 + 3)*(c_Cam10Thk20 - c_Cam15Thk20) + c_Cam15Thk20;

          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }
      else if (-10.0 <= camber && camber < -5.0)
        {
          c_Thk5 = (camber/5 + 2)*(c_Cam5Thk5 - c_Cam10Thk5) + c_Cam10Thk5;
          c_Thk10 = (camber/5 + 2)*(c_Cam5Thk10 - c_Cam10Thk10) + c_Cam10Thk10;
          c_Thk15 = (camber/5 + 2)*(c_Cam5Thk15 - c_Cam10Thk15) + c_Cam10Thk15;
          c_Thk20 = (camber/5 + 2)*(c_Cam5Thk20 - c_Cam10Thk20) + c_Cam10Thk20;

          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }
      else if (-5.0 <= camber && camber < 0)
        {
          c_Thk5 = (camber/5 + 1)*(c_Cam0Thk5 - c_Cam5Thk5) + c_Cam5Thk5;
          c_Thk10 = (camber/5 + 1)*(c_Cam0Thk10 - c_Cam5Thk10) + c_Cam5Thk10;
          c_Thk15 = (camber/5 + 1)*(c_Cam0Thk15 - c_Cam5Thk15) + c_Cam5Thk15;
          c_Thk20 = (camber/5 + 1)*(c_Cam0Thk20 - c_Cam5Thk20) + c_Cam5Thk20;

          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }
      else if (0 <= camber && camber < 5)
        {
          c_Thk5 = (camber/5)*(c_Cam5Thk5 - c_Cam0Thk5) + c_Cam0Thk5;
          c_Thk10 = (camber/5)*(c_Cam5Thk10 - c_Cam0Thk10) + c_Cam0Thk10;
          c_Thk15 = (camber/5)*(c_Cam5Thk15 - c_Cam0Thk15) + c_Cam0Thk15;
          c_Thk20 = (camber/5)*(c_Cam5Thk20 - c_Cam0Thk20) + c_Cam0Thk20;

          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }
      else if (5 <= camber && camber < 10)
        {
          c_Thk5 = (camber/5 - 1)*(c_Cam10Thk5 - c_Cam5Thk5) + c_Cam5Thk5;
          c_Thk10 = (camber/5 - 1)*(c_Cam10Thk10 - c_Cam5Thk10) + c_Cam5Thk10;
          c_Thk15 = (camber/5 - 1)*(c_Cam10Thk15 - c_Cam5Thk15) + c_Cam5Thk15;
          c_Thk20 = (camber/5 - 1)*(c_Cam10Thk20 - c_Cam5Thk20) + c_Cam5Thk20;

          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }
      else if (10 <= camber && camber < 15)
        {
          c_Thk5 = (camber/5 - 2)*(c_Cam15Thk5 - c_Cam10Thk5) + c_Cam10Thk5;
          c_Thk10 = (camber/5 - 2)*(c_Cam15Thk10 - c_Cam10Thk10) + c_Cam10Thk10;
          c_Thk15 = (camber/5 - 2)*(c_Cam15Thk15 - c_Cam10Thk15) + c_Cam10Thk15;
          c_Thk20 = (camber/5 - 2)*(c_Cam15Thk20 - c_Cam10Thk20) + c_Cam10Thk20;

          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }
      else if (15 <= camber && camber <= 20)
        {
          c_Thk5 = (camber/5 - 3)*(c_Cam20Thk5 - c_Cam15Thk5) + c_Cam15Thk5;
          c_Thk10 = (camber/5 - 3)*(c_Cam20Thk10 - c_Cam15Thk10) + c_Cam15Thk10;
          c_Thk15 = (camber/5 - 3)*(c_Cam20Thk15 - c_Cam15Thk15) + c_Cam15Thk15;
          c_Thk20 = (camber/5 - 3)*(c_Cam20Thk20 - c_Cam15Thk20) + c_Cam15Thk20;

          if (1.0 <= thickness && thickness <= 5.0)
            {
              c = c_Thk5;
            }
          else if (5.0 < thickness && thickness <= 10.0)
            {
              c = (thickness/5 - 1)*(c_Thk10 - c_Thk5) + c_Thk5;
            }
          else if (10.0 < thickness && thickness <= 15.0)
            {
              c = (thickness/5 - 2)*(c_Thk15 - c_Thk10) + c_Thk10;
            }
          else if (15.0 < thickness && thickness <= 20.0)
            {
              c = (thickness/5 - 3)*(c_Thk20 - c_Thk15) + c_Thk15;
            }
        }

      // System.out.println("-- aoa: " + aoa + " camber: " + camber + " c: " + c);
      // allow re and aspect corrections if (true) return; 
      return c;
    }

    // Compute 15-elt array of Cl values for given cl tables, thickness and camber.
    // Can be used to load cache current_part.t_Cl
    double[] compute_t_Cl (double[][] t_lift, double thickness, double camber) {
      double[] result = new double[15];
      if (camber >= 0)
        for (int aoa = -28, i = 0; aoa <= 28; aoa += 4, i++) 
          result[i] = ci15_from_javafoil_data(t_lift, aoa, thickness, camber);
      else 
        for (int aoa = -28, i = 0; aoa <= 28; aoa += 4, i++) 
          result[i] = -ci15_from_javafoil_data(t_lift, -aoa, thickness, camber);
      return result;
    }

    // for drag interpolator
    double recyl[]  = {.1, .2, .4, .5, .6, .8, 1.0,
                       2.0, 4.0, 5.0, 6.0, 8.0, 10.0,
                       20.0, 40.0, 50.0, 60.0, 80.0, 100.0,
                       200.0, 400.0, 500.0, 600.0, 800.0, 1000.,
                       2000., 4000., 5000., 6000., 8000., 10000.,
                       100000.,200000.,400000.,500000.,600000.,800000.,1000000.,
                       2000000.,4000000.,5000000.,6000000.,8000000.,1000000000000. }; 
    double cdcyl[]  = {70., 35., 20., 17., 15., 13., 10.,
                       7., 5.5, 5.0, 4.5, 4., 3.5,
                       3.0, 2.7, 2.5, 2.0, 2.0, 1.9,
                       1.6, 1.4, 1.2, 1.1, 1.1, 1.0, 
                       1.2, 1.4, 1.4, 1.5, 1.5, 1.6,
                       1.6, 1.4, .4, .28, .32, .4, .45,
                       .6, .8, .8, .85, .9, .9 }; 
    double resps[]  = {.1, .2, .4, .5, .6, .8, 1.0,
                       2.0, 4.0, 5.0, 6.0, 8.0, 10.0,
                       20., 40., 50., 60., 80.0, 100.0,
                       200., 400., 500., 600., 800.0, 1000.,
                       2000., 4000., 5000., 6000., 8000., 10000.,
                       20000., 40000., 50000., 60000., 80000., 100000.,
                       200000., 400000., 500000., 600000., 800000., 1000000.,
                       2000000., 4000000., 5000000., 6000000., 8000000., 1000000000000. }; 

    double cdsps[]  = {270., 110., 54., 51., 40., 35., 28.,
                       15., 8.5, 7.5, 6.0, 5.4, 4.9,
                       3.1, 1.9, 1.8, 1.5, 1.3, 1.1,
                       0.81, 0.6, 0.58, 0.56, 0.5, 0.49, 
                       0.40, 0.41, 0.415, 0.42, 0.43, 0.44,
                       0.44, 0.45, 0.455, 0.46, 0.47, 0.48, 
                       0.47, 0.10, 0.098, 0.1, 0.15, 0.19, 
                       0.30, 0.35, 0.370, 0.4, 0.40, 0.42 }; 
    double cdspr[]  = {270., 110., 54., 51., 40., 35., 28.,
                       15., 8.5, 7.5, 6.0, 5.4, 4.9,
                       3.1, 1.9, 1.8, 1.5, 1.3, 1.1,
                       0.81, 0.6, 0.58, 0.56, 0.5, 0.49, 
                       0.40, 0.41, 0.415, 0.42, 0.43, 0.44,
                       0.44, 0.45, 0.455, 0.46, 0.42, 0.15, 
                       0.27, 0.33, 0.35, 0.37, 0.38, 0.39, 
                       0.40, 0.41, 0.41, 0.42, 0.43, 0.44 }; 
    double cdspg[]  = {270., 110., 54., 51., 40., 35., 28.,
                       15., 8.5, 7.5, 6.0, 5.4, 4.9,
                       3.1, 1.9, 1.8, 1.5, 1.3, 1.1,
                       0.81, 0.6, 0.58, 0.56, 0.5, 0.49, 
                       0.40, 0.41, 0.415, 0.42, 0.43, 0.44,
                       0.44, 0.28, 0.255, 0.24, 0.24, 0.25, 
                       0.26, 0.27, 0.290, 0.33, 0.37, 0.40, 
                       0.41, 0.42, 0.420, 0.43, 0.44, 0.45 }; 


    // Drag Interpolator
    // effaoa is in dgegrees
    // thickness and camber are in %
    // this is for Joukowski, flat and ellipse
    public double getCdragAnalytical (double cldin, double effaoa, double thickness, double camber)
    {
      double dragco = 0;
      int index,ifound;  
      double dragCam0Thk5, dragCam5Thk5, dragCam10Thk5, dragCam15Thk5, dragCam20Thk5;
      double dragCam0Thk10, dragCam5Thk10, dragCam10Thk10, dragCam15Thk10, dragCam20Thk10;
      double dragCam0Thk15, dragCam5Thk15, dragCam10Thk15, dragCam15Thk15, dragCam20Thk15;
      double dragCam0Thk20, dragCam5Thk20, dragCam10Thk20, dragCam15Thk20, dragCam20Thk20;
      double dragThk5, dragThk10, dragThk15, dragThk20;
      double dragCam0, dragCam5, dragCam10, dragCam15, dragCam20;  //used for the flat plate drag values

        if (current_part.foil == FOIL_JOUKOWSKI)    //airfoil drag logic
          {
            //switch (in.anl.foil_drag_comp_method) {
            //case DRAG_COMP_POLY_FOILSIMORIG: {
            // thickness 5%
            dragCam0Thk5 = -9E-07*Math.pow(effaoa,3) + 0.0007*Math.pow(effaoa,2) + 0.0008*effaoa + 0.015;
            dragCam5Thk5 = 4E-08*Math.pow(effaoa,5) - 7E-07*Math.pow(effaoa,4) - 1E-05*Math.pow(effaoa,3) + 0.0009*Math.pow(effaoa,2) + 0.0033*effaoa + 0.0301;
            dragCam10Thk5 = -9E-09*Math.pow(effaoa,6) - 6E-08*Math.pow(effaoa,5) + 5E-06*Math.pow(effaoa,4) + 3E-05*Math.pow(effaoa,3) - 0.0001*Math.pow(effaoa,2) - 0.0025*effaoa + 0.0615;
            dragCam15Thk5 = 8E-10*Math.pow(effaoa,6) - 5E-08*Math.pow(effaoa,5) - 1E-06*Math.pow(effaoa,4) + 3E-05*Math.pow(effaoa,3) + 0.0008*Math.pow(effaoa,2) - 0.0027*effaoa + 0.0612;
            dragCam20Thk5 = 8E-9*Math.pow(effaoa,6) + 1E-8*Math.pow(effaoa,5) - 5E-6*Math.pow(effaoa,4) + 6E-6*Math.pow(effaoa,3) + 0.001*Math.pow(effaoa,2) - 0.001*effaoa + 0.1219;

            // thickness 10%
            dragCam0Thk10 = -1E-08*Math.pow(effaoa,6) + 6E-08*Math.pow(effaoa,5) + 6E-06*Math.pow(effaoa,4) - 2E-05*Math.pow(effaoa,3) - 0.0002*Math.pow(effaoa,2) + 0.0017*effaoa + 0.0196;
            dragCam5Thk10 = 3E-09*Math.pow(effaoa,6) + 6E-08*Math.pow(effaoa,5) - 2E-06*Math.pow(effaoa,4) - 3E-05*Math.pow(effaoa,3) + 0.0008*Math.pow(effaoa,2) + 0.0038*effaoa + 0.0159;
            dragCam10Thk10 = -5E-09*Math.pow(effaoa,6) - 3E-08*Math.pow(effaoa,5) + 2E-06*Math.pow(effaoa,4) + 1E-05*Math.pow(effaoa,3) + 0.0004*Math.pow(effaoa,2) - 3E-05*effaoa + 0.0624;
            dragCam15Thk10 = -2E-09*Math.pow(effaoa,6) - 2E-08*Math.pow(effaoa,5) - 5E-07*Math.pow(effaoa,4) + 8E-06*Math.pow(effaoa,3) + 0.0009*Math.pow(effaoa,2) + 0.0034*effaoa + 0.0993;
            dragCam20Thk10 = 2E-09*Math.pow(effaoa,6) - 3E-08*Math.pow(effaoa,5) - 2E-06*Math.pow(effaoa,4) + 2E-05*Math.pow(effaoa,3) + 0.0009*Math.pow(effaoa,2) + 0.0023*effaoa + 0.1581;

            // thickness 15%
            dragCam0Thk15 = -5E-09*Math.pow(effaoa,6) + 7E-08*Math.pow(effaoa,5) + 3E-06*Math.pow(effaoa,4) - 3E-05*Math.pow(effaoa,3) - 7E-05*Math.pow(effaoa,2) + 0.0017*effaoa + 0.0358;
            dragCam5Thk15 = -4E-09*Math.pow(effaoa,6) - 8E-09*Math.pow(effaoa,5) + 2E-06*Math.pow(effaoa,4) - 9E-07*Math.pow(effaoa,3) + 0.0002*Math.pow(effaoa,2) + 0.0031*effaoa + 0.0351;
            dragCam10Thk15 = 3E-09*Math.pow(effaoa,6) + 3E-08*Math.pow(effaoa,5) - 2E-06*Math.pow(effaoa,4) - 1E-05*Math.pow(effaoa,3) + 0.0009*Math.pow(effaoa,2) + 0.004*effaoa + 0.0543;
            dragCam15Thk15 = 3E-09*Math.pow(effaoa,6) + 5E-08*Math.pow(effaoa,5) - 2E-06*Math.pow(effaoa,4) - 3E-05*Math.pow(effaoa,3) + 0.0008*Math.pow(effaoa,2) + 0.0087*effaoa + 0.1167;
            dragCam20Thk15 = 3E-10*Math.pow(effaoa,6) - 3E-08*Math.pow(effaoa,5) - 6E-07*Math.pow(effaoa,4) + 3E-05*Math.pow(effaoa,3) + 0.0006*Math.pow(effaoa,2) + 0.0008*effaoa + 0.1859;

            // thickness 20%
            dragCam0Thk20 = -3E-09*Math.pow(effaoa,6) + 2E-08*Math.pow(effaoa,5) + 2E-06*Math.pow(effaoa,4) - 8E-06*Math.pow(effaoa,3) - 4E-05*Math.pow(effaoa,2) + 0.0003*effaoa + 0.0416;
            dragCam5Thk20 = -3E-09*Math.pow(effaoa,6) - 7E-08*Math.pow(effaoa,5) + 1E-06*Math.pow(effaoa,4) + 3E-05*Math.pow(effaoa,3) + 0.0004*Math.pow(effaoa,2) + 5E-05*effaoa + 0.0483;
            dragCam10Thk20 = 1E-08*Math.pow(effaoa,6) + 4E-08*Math.pow(effaoa,5) - 6E-06*Math.pow(effaoa,4) - 2E-05*Math.pow(effaoa,3) + 0.0014*Math.pow(effaoa,2) + 0.007*effaoa + 0.0692;
            dragCam15Thk20 = 3E-09*Math.pow(effaoa,6) - 9E-08*Math.pow(effaoa,5) - 3E-06*Math.pow(effaoa,4) + 4E-05*Math.pow(effaoa,3) + 0.001*Math.pow(effaoa,2) + 0.0021*effaoa + 0.139;
            dragCam20Thk20 = 3E-09*Math.pow(effaoa,6) - 7E-08*Math.pow(effaoa,5) - 3E-06*Math.pow(effaoa,4) + 4E-05*Math.pow(effaoa,3) + 0.0012*Math.pow(effaoa,2) + 0.001*effaoa + 0.1856;

            if (-20.0 <= camber && camber < -15.0)
              {
                dragThk5 = (camber/5 + 4)*(dragCam15Thk5 - dragCam20Thk5) + dragCam20Thk5;
                dragThk10 = (camber/5 + 4)*(dragCam15Thk10 - dragCam20Thk10) + dragCam20Thk10;
                dragThk15 = (camber/5 + 4)*(dragCam15Thk15 - dragCam20Thk15) + dragCam20Thk15;
                dragThk20 = (camber/5 + 4)*(dragCam15Thk20 - dragCam20Thk20) + dragCam20Thk20;
                    
                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  { // dragco = two_pt(5.0, 10.0, dragThk5, dragThk10)
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
            else if (-15.0 <= camber && camber < -10.0)
              {
                dragThk5 = (camber/5 + 3)*(dragCam10Thk5 - dragCam15Thk5) + dragCam15Thk5;
                dragThk10 = (camber/5 + 3)*(dragCam10Thk10 - dragCam15Thk10) + dragCam15Thk10;
                dragThk15 = (camber/5 + 3)*(dragCam10Thk15 - dragCam15Thk15) + dragCam15Thk15;
                dragThk20 = (camber/5 + 3)*(dragCam10Thk20 - dragCam15Thk20) + dragCam15Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
            else if (-10.0 <= camber && camber < -5.0)
              {
                dragThk5 = (camber/5 + 2)*(dragCam5Thk5 - dragCam10Thk5) + dragCam10Thk5;
                dragThk10 = (camber/5 + 2)*(dragCam5Thk10 - dragCam10Thk10) + dragCam10Thk10;
                dragThk15 = (camber/5 + 2)*(dragCam5Thk15 - dragCam10Thk15) + dragCam10Thk15;
                dragThk20 = (camber/5 + 2)*(dragCam5Thk20 - dragCam10Thk20) + dragCam10Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
            else if (-5.0 <= camber && camber < 0)
              {
                dragThk5 = (camber/5 + 1)*(dragCam0Thk5 - dragCam5Thk5) + dragCam5Thk5;
                dragThk10 = (camber/5 + 1)*(dragCam0Thk10 - dragCam5Thk10) + dragCam5Thk10;
                dragThk15 = (camber/5 + 1)*(dragCam0Thk15 - dragCam5Thk15) + dragCam5Thk15;
                dragThk20 = (camber/5 + 1)*(dragCam0Thk20 - dragCam5Thk20) + dragCam5Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
            else if (0 <= camber && camber < 5)
              {
                dragThk5 = (camber/5)*(dragCam5Thk5 - dragCam0Thk5) + dragCam0Thk5;
                dragThk10 = (camber/5)*(dragCam5Thk10 - dragCam0Thk10) + dragCam0Thk10;
                dragThk15 = (camber/5)*(dragCam5Thk15 - dragCam0Thk15) + dragCam0Thk15;
                dragThk20 = (camber/5)*(dragCam5Thk20 - dragCam0Thk20) + dragCam0Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
            else if (5 <= camber && camber < 10)
              {
                dragThk5 = (camber/5 - 1)*(dragCam10Thk5 - dragCam5Thk5) + dragCam5Thk5;
                dragThk10 = (camber/5 - 1)*(dragCam10Thk10 - dragCam5Thk10) + dragCam5Thk10;
                dragThk15 = (camber/5 - 1)*(dragCam10Thk15 - dragCam5Thk15) + dragCam5Thk15;
                dragThk20 = (camber/5 - 1)*(dragCam10Thk20 - dragCam5Thk20) + dragCam5Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
            else if (10 <= camber && camber < 15)
              {
                dragThk5 = (camber/5 - 2)*(dragCam15Thk5 - dragCam10Thk5) + dragCam10Thk5;
                dragThk10 = (camber/5 - 2)*(dragCam15Thk10 - dragCam10Thk10) + dragCam10Thk10;
                dragThk15 = (camber/5 - 2)*(dragCam15Thk15 - dragCam10Thk15) + dragCam10Thk15;
                dragThk20 = (camber/5 - 2)*(dragCam15Thk20 - dragCam10Thk20) + dragCam10Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
            else if (15 <= camber && camber <= 20)
              {
                dragThk5 = (camber/5 - 3)*(dragCam20Thk5 - dragCam15Thk5) + dragCam15Thk5;
                dragThk10 = (camber/5 - 3)*(dragCam20Thk10 - dragCam15Thk10) + dragCam15Thk10;
                dragThk15 = (camber/5 - 3)*(dragCam20Thk15 - dragCam15Thk15) + dragCam15Thk15;
                dragThk20 = (camber/5 - 3)*(dragCam20Thk20 - dragCam15Thk20) + dragCam15Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 15.0)
                  {
                    dragco = (thickness/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
                  }
                else if (15.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
                  }
              }
          } else if (current_part.foil == FOIL_ELLIPTICAL)   //elliptical drag logic
          {
            dragCam0Thk5 = -6E-07*Math.pow(effaoa,3) + 0.0007*Math.pow(effaoa,2) + 0.0007*effaoa + 0.0428;
            dragCam10Thk5 = 5E-09*Math.pow(effaoa,6) - 7E-08*Math.pow(effaoa,5) - 3E-06*Math.pow(effaoa,4) + 5E-05*Math.pow(effaoa,3) + 0.0009*Math.pow(effaoa,2) - 0.0058*effaoa + 0.0758;
            dragCam20Thk5 = 1E-08*Math.pow(effaoa,6) - 2E-08*Math.pow(effaoa,5) - 7E-06*Math.pow(effaoa,4) + 1E-05*Math.pow(effaoa,3) + 0.0015*Math.pow(effaoa,2) + 0.0007*effaoa + 0.1594;
                    
            dragCam0Thk10 = 3E-09*Math.pow(effaoa,6) + 4E-08*Math.pow(effaoa,5) - 3E-06*Math.pow(effaoa,4) - 9E-06*Math.pow(effaoa,3) + 0.0013*Math.pow(effaoa,2) + 0.0007*effaoa + 0.0112;
            dragCam10Thk10 = -4E-09*Math.pow(effaoa,6) - 9E-08*Math.pow(effaoa,5) + 2E-06*Math.pow(effaoa,4) + 7E-05*Math.pow(effaoa,3) + 0.0008*Math.pow(effaoa,2) - 0.0095*effaoa + 0.0657;
            dragCam20Thk10 = -8E-09*Math.pow(effaoa,6) - 9E-08*Math.pow(effaoa,5) + 3E-06*Math.pow(effaoa,4) + 6E-05*Math.pow(effaoa,3) + 0.0005*Math.pow(effaoa,2) - 0.0088*effaoa + 0.2088;

            dragCam0Thk20 = -7E-09*Math.pow(effaoa,6) - 1E-07*Math.pow(effaoa,5) + 4E-06*Math.pow(effaoa,4) + 6E-05*Math.pow(effaoa,3) + 0.0001*Math.pow(effaoa,2) - 0.0087*effaoa + 0.0596;
            dragCam10Thk20 = -2E-09*Math.pow(effaoa,6) + 2E-07*Math.pow(effaoa,5) + 1E-06*Math.pow(effaoa,4) - 6E-05*Math.pow(effaoa,3) + 0.0004*Math.pow(effaoa,2) - 7E-05*effaoa + 0.1114;
            dragCam20Thk20 = 4E-09*Math.pow(effaoa,6) - 7E-08*Math.pow(effaoa,5) - 3E-06*Math.pow(effaoa,4) + 3E-05*Math.pow(effaoa,3) + 0.001*Math.pow(effaoa,2) - 0.0018*effaoa + 0.1925;

            if (-20.0 <= camber && camber < -10.0)
              {
                dragThk5 = (camber/10 + 2)*(dragCam10Thk5 - dragCam20Thk5) + dragCam20Thk5;
                dragThk10 = (camber/10 + 2)*(dragCam10Thk10 - dragCam20Thk10) + dragCam20Thk10;
                dragThk20 = (camber/10 + 2)*(dragCam10Thk20 - dragCam20Thk20) + dragCam20Thk20;
                    
                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/10 - 1)*(dragThk20 - dragThk10) + dragThk10;
                  }
              }
            else if (-10.0 <= camber && camber < 0)
              {
                dragThk5 = (camber/10 + 1)*(dragCam0Thk5 - dragCam10Thk5) + dragCam10Thk5;
                dragThk10 = (camber/10 + 1)*(dragCam0Thk10 - dragCam10Thk10) + dragCam10Thk10;
                dragThk20 = (camber/10 + 1)*(dragCam0Thk20 - dragCam10Thk20) + dragCam10Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/10 - 1)*(dragThk20 - dragThk10) + dragThk10;
                  }
              }
            else if (0 <= camber && camber < 10)
              {
                dragThk5 = (camber/10)*(dragCam10Thk5 - dragCam0Thk5) + dragCam0Thk5;
                dragThk10 = (camber/10)*(dragCam10Thk10 - dragCam0Thk10) + dragCam0Thk10;
                dragThk20 = (camber/10)*(dragCam10Thk20 - dragCam0Thk20) + dragCam0Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/10 - 1)*(dragThk20 - dragThk10) + dragThk10;
                  }
              }
            else if (10 <= camber && camber < 20)
              {
                dragThk5 = (camber/10 - 1)*(dragCam20Thk5 - dragCam10Thk5) + dragCam10Thk5;
                dragThk10 = (camber/10 - 1)*(dragCam20Thk10 - dragCam10Thk10) + dragCam10Thk10;
                dragThk20 = (camber/10 - 1)*(dragCam20Thk20 - dragCam10Thk20) + dragCam10Thk20;

                if (1.0 <= thickness && thickness <= 5.0)
                  {
                    dragco = dragThk5;
                  }
                else if (5.0 < thickness && thickness <= 10.0)
                  {
                    dragco = (thickness/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
                  }
                else if (10.0 < thickness && thickness <= 20.0)
                  {
                    dragco = (thickness/10 - 1)*(dragThk20 - dragThk10) + dragThk10;
                  }
              }

          } // end of FOIL_ELLIPTICAL
        else if (current_part.foil == FOIL_FLAT_PLATE)    //flat plate drag logic
          {
            dragCam0 = -9E-07*Math.pow(effaoa,3) + 0.0007*Math.pow(effaoa,2) + 0.0008*effaoa + 0.015;
            dragCam5 = 1E-08*Math.pow(effaoa,6) + 4E-08*Math.pow(effaoa,5) - 9E-06*Math.pow(effaoa,4) - 1E-05*Math.pow(effaoa,3) + 0.0021*Math.pow(effaoa,2) + 0.0033*effaoa + 0.006;
            dragCam10 = -9E-09*Math.pow(effaoa,6) - 6E-08*Math.pow(effaoa,5) + 5E-06*Math.pow(effaoa,4) + 3E-05*Math.pow(effaoa,3) - 0.0001*Math.pow(effaoa,2) - 0.0025*effaoa + 0.0615;
            dragCam15 = 8E-10*Math.pow(effaoa,6) - 5E-08*Math.pow(effaoa,5) - 1E-06*Math.pow(effaoa,4) + 3E-05*Math.pow(effaoa,3) + 0.0008*Math.pow(effaoa,2) - 0.0027*effaoa + 0.0612;
            dragCam20 = 8E-9*Math.pow(effaoa,6) + 1E-8*Math.pow(effaoa,5) - 5E-6*Math.pow(effaoa,4) + 6E-6*Math.pow(effaoa,3) + 0.001*Math.pow(effaoa,2) - 0.001*effaoa + 0.1219;

            if (-20.0 <= camber && camber < -15.0)
              {
                dragco = (camber/5 + 4)*(dragCam15 - dragCam20) + dragCam20;
              }
            else if (-15.0 <= camber && camber < -10.0)
              {
                dragco = (camber/5 + 3)*(dragCam10 - dragCam15) + dragCam15;
              }
            else if (-10.0 <= camber && camber < -5.0)
              {
                dragco = (camber/5 + 2)*(dragCam5 - dragCam10) + dragCam10;
              }
            else if (-5.0 <= camber && camber < 0)
              {
                dragco = (camber/5 + 1)*(dragCam0 - dragCam5) + dragCam5;
              }
            else if (0 <= camber && camber < 5)
              {
                dragco = (camber/5)*(dragCam5 - dragCam0) + dragCam0;
              }
            else if (5 <= camber && camber < 10)
              {
                dragco = (camber/5 - 1)*(dragCam10 - dragCam5) + dragCam5;
              }
            else if (10 <= camber && camber < 15)
              {
                dragco = (camber/5 - 2)*(dragCam15 - dragCam10) + dragCam10;
              }
            else if (15 <= camber && camber <= 20)
              {
                dragco = (camber/5 - 3)*(dragCam20 - dragCam15) + dragCam15;
              }

          } // end of FOIL_FLAT_PLATE
        return dragco;
    }


    // Drag Interpolator
    // effaoa is in dgegrees
    // thickness and camber are in %
    // this is for cylinder and ball shapes
    public double getCdragRound (double cldin, double effaoa, double thickness, double camber) {
      double dragco = 0;
      int index,ifound;  
      double dragCam0Thk5, dragCam5Thk5, dragCam10Thk5, dragCam15Thk5, dragCam20Thk5;
      double dragCam0Thk10, dragCam5Thk10, dragCam10Thk10, dragCam15Thk10, dragCam20Thk10;
      double dragCam0Thk15, dragCam5Thk15, dragCam10Thk15, dragCam15Thk15, dragCam20Thk15;
      double dragCam0Thk20, dragCam5Thk20, dragCam10Thk20, dragCam15Thk20, dragCam20Thk20;
      double dragThk5, dragThk10, dragThk15, dragThk20;
      double dragCam0, dragCam5, dragCam10, dragCam15, dragCam20;  //used for the flat plate drag values
      double reynolds = current_part.reynolds;
      if (current_part.foil == FOIL_CYLINDER) {  //cylinder drag logic
        ifound = 0;
        for (index = 0; index <= 43; ++ index) {
          if (reynolds >= recyl[index] && reynolds < recyl[index+1]) ifound = index;
        }

        dragco = ((cdcyl[ifound+1]-cdcyl[ifound])/(recyl[ifound+1]-recyl[ifound]))*
          (reynolds - recyl[ifound]) + cdcyl[ifound];
      } else if (current_part.foil == FOIL_BALL) {  //sphere drag logic
        ifound = 0;
        for (index = 0; index <= 48; ++ index) {
          if (reynolds >= resps[index] && reynolds < resps[index+1]) ifound = index;
        }

        switch (bdragflag) {
        case 1: // smooth ball
          dragco = ((cdsps[ifound+1]-cdsps[ifound])/(resps[ifound+1]-resps[ifound]))*
            (reynolds - resps[ifound]) + cdsps[ifound];
          break;
        case 2: // rough ball
          dragco = ((cdspr[ifound+1]-cdspr[ifound])/(resps[ifound+1]-resps[ifound]))*
            (reynolds - resps[ifound]) + cdspr[ifound];
          break;
        default: case 3: // golf ball
          dragco = ((cdspg[ifound+1]-cdspg[ifound])/(resps[ifound+1]-resps[ifound]))*
            (reynolds - resps[ifound]) + cdspg[ifound];
        }
      }
        return dragco;
    }

    public double get_Cd (double cldin, double effaoa, double thickness, double camber) {
      if (stall_model_type == STALL_MODEL_IDEAL_FLOW)
        return 0;
      return current_part.foil.get_Cd(cldin, effaoa, thickness, camber);
    }

  } // end Solver


  class VPP {     // Velocity Prediction Procedures aka VPP
    boolean trace;
    void trace (String msg) {
      if (trace) System.out.println("-- VPP trace: " + msg);
    }

    // for heading angle estimate, symmetric airfoil. That is, returns AoA of the mast the mast must 
    // for given side-force (lift). This is not eact and only Ok for linear region of small angles, used
    // in conjunction with main VPP routines to set some reasonable mast AoA.
    // The param alpha_to_cl_slope is typically 0.1...0.15 and can be set 
    // as 0.009*strut.thickness.....
    double find_aoa_of_given_lift (double lift, double cl_to_alpha) {
      // L = Cl * A * .5 * rho * V^2 , 
      // in foilsim. L = fconv * current_part.cl * q0_EN * area / lconv / lconv; 
      // so...
      double cl_required = lift / ( q0_SI * strut.span * strut.chord * (1-alt/100));
      double aoa = cl_required * cl_to_alpha;
      // NEED to DEBUG the incremental increse in above!!
      // but for now, the above ad hoc formula gives OK approximation, use it...
      // double k_alt = (Math.pow(alt/100, 1.7));
      // return 0.9 * k_alt;
      return aoa;
    }

    boolean set_mast_aoa_for_given_drag_auto = false;

    void set_mast_aoa_for_given_drag (double drag) {
      if (!set_mast_aoa_for_given_drag_auto) return;

      // DEBUG suspect bug somehwere, incrementa aoa increase...
      //if (drag > 0) return;
      // drag = 85; //Newtons

      // for simplicity, assume kite/sail pulls at 45 degrees laterally
      double mast_aoa = find_aoa_of_given_lift(drag, 10); // 10: make it thickness-dependent
      mast_aoa = limit(ang_min, mast_aoa, ang_max);
      if (current_part == strut) current_part.aoa = mast_aoa;
      else strut.aoa = mast_aoa;
      con.recomp_all_parts();
    }


    void find_aoa_of_min_drag_slow () {
      // preamble: make sure inputs are in
      //computeFlowAndRegenPlotAndAdjust();
      con.recomp_all_parts();

      double min_drag = total_drag();
      double min_drag_aoa = craft_pitch;
      for (double p = ang_min/2; p < ang_max; p += 0.1) {
        craft_pitch = p;
        //computeFlowAndRegenPlotAndAdjust();
        con.recomp_all_parts();
        if (total_drag() < min_drag)
          min_drag_aoa = craft_pitch;
      }
      craft_pitch = min_drag_aoa;
      //computeFlowAndRegenPlotAndAdjust();
      con.recomp_all_parts();
    }


    // pivoting algorithm
    void find_aoa_of_min_drag () {
      // preamble: make sure inputs are in
      //computeFlowAndRegenPlotAndAdjust();
      con.recomp_all_parts();

      double min_drag = total_drag();
      double min_drag_aoa = craft_pitch;
      double step = 0.5;
      double prev_drag = min_drag;
      double p = Math.min(min_drag_aoa + step, ang_max - 4*step);
      while (p < ang_max && p > ang_min) {
        craft_pitch = p;
        //computeFlowAndRegenPlotAndAdjust();
        con.recomp_all_parts();
        double drag = total_drag();
        if (drag > prev_drag) { // pivot
          // do not! p -= step; // go back 1 'old' step 
          step = -step/2; // shrink step
          if (Math.abs(step) < 0.05) break;
        } else if (drag < min_drag) {
          min_drag_aoa = craft_pitch;
        }
        prev_drag = drag;
        p += step;
      }
      craft_pitch = min_drag_aoa;
      //computeFlowAndRegenPlotAndAdjust();
      con.recomp_all_parts();
    }


    // the idea here is start with speed 10 and increment 2
    // and invoke steady_flight_at_given_speed. If 
    // the pitch at max (and lift not enough) keep going increasing the speed
    // if flying than pivot ( step = step * -0.5 ) and in that direction 
    // pivot when nto flying. finish when step is + and small enough
    void find_min_takeoff_v (double min_lift, double max_drag) {
      double saved_speed = velocity;
      // guess a reasonable starting speed to iterate
      // by setting pitch to 10 and finding speed at enough lift,
      double init_speed = 10;
      double speed = init_speed;
      double step = 2;
      double pitch = 0;

      while (speed < v_max) {
        velocity = speed;
        // System.out.println("-- velocity: " + velocity);
        vpp.steady_flight_at_given_speed(5, 0);
        // double lift = total_lift();
        if (step > 0 && vpp.steady_flight_at_given_speed___ok && total_drag() <= max_drag) {
          if (step < 0.01) break;
          step = -step/2; // shrink step & pivot
        } else if (step < 0 && (vpp.steady_flight_at_given_speed___ok == false || total_drag() > max_drag)) { 
          step = -step/2; // shrink step & pivot
        } // else keep going with current step
        speed += step;
      }

      if (total_drag() > max_drag) 
        System.out.println(min_takeoff_speed_info = "Falure! Decrease load or increse drag");
      else {
        make_min_takeoff_speed_info(min_lift, max_drag, velocity); 
        System.out.println("\nDone!\n----------------\n" + min_takeoff_speed_info);
      }

    }


    void find_min_takeoff_v_old_algorithm (double min_lift, double max_drag) {
      double saved_speed = velocity;
      // steap one. guess a reasonable starting speed to iterate
      // by setting pitch to 10 and finding speed at enough lift,
      double speed = 5;
      double pitch = 10;
      for (; speed < v_max; speed += 1) {
        velocity = speed;
        // System.out.println("-- velocity: " + velocity);
        craft_pitch = pitch;
        //computeFlowAndRegenPlotAndAdjust();
        con.recomp_all_parts();
        if (total_lift() >= min_lift) 
          break;
      }


      pitch = 0;
      double speed_step = 2, pitch_step = 0.25;
      while(0 < (speed=find_min_takeoff_v_old_algorithm_aux(min_lift, max_drag, speed, speed_step, pitch, pitch_step))) {
        pitch = craft_pitch - Math.abs(craft_pitch/3);
        speed_step /=2;
        pitch_step /=2;
      }
      if (speed == -2) {
        System.out.println("Can not takeoff with such small drag requirement, increase it.");
        velocity = saved_speed;
        //computeFlowAndRegenPlotAndAdjust();
        con.recomp_all_parts();
      }
    }

    // returns negative speed when done or failed to satisfy the constraints
    // otherwise returns speed to re-iterate from.
    double find_min_takeoff_v_old_algorithm_aux (double min_lift, double max_drag, double speed, double speed_step, double pitch_init, double pitch_step) {
      double pitch = pitch_init;
      double pitch_limit = ang_max*0.7;
      {
        trace("speed_step: " + speed_step + " pitch_step: " + pitch_step);
        for (; speed < v_max; speed += speed_step) {
          velocity = speed;
          // if the startimg pitch guess when at this point was too high, 
          // drag will be too big at this point; then reduce pitch
          while (pitch > ang_min/2) {
            craft_pitch = pitch;
            //computeFlowAndRegenPlotAndAdjust();
            con.recomp_all_parts();
            if (total_drag() >= max_drag)
              pitch = Math.max(pitch_init, craft_pitch - Math.abs(craft_pitch/4));
            else 
              break; // while (pitch > ang_min/2)
          }              
              
          trace("for() speed: " + speed + " pitch: " + pitch);
          for (;pitch < pitch_limit; pitch += pitch_step) {
            craft_pitch = pitch;
            //computeFlowAndRegenPlotAndAdjust();
            con.recomp_all_parts();
            if (total_drag() >= max_drag) {
              // stop increasing pitch, won't work. need more speed
              trace("too much drag. speed: " + speed + " pitch: " + pitch + " lift " + total_lift());
              pitch = Math.max(pitch_init, craft_pitch - Math.abs(craft_pitch/4));
              break; // for (pitch...)
            }
            if (total_lift() >= min_lift) {
              if (speed_step > 0.25) {
                trace("request reiterate. speed: " + speed + " pitch: " + pitch + " lift " + total_lift() + " speed_step: " + speed_step);
                return Math.max(0, velocity - speed_step); // request to reiterate
              } else {
                // done!
                make_min_takeoff_speed_info(min_lift, max_drag, velocity); 
                System.out.println("\nDone!\n----------------\n" + min_takeoff_speed_info);
                return -1;
              }
            } 
          }
          // need to start from lowest pitch?
          if (pitch >= pitch_limit) pitch = pitch_init;
        }
      }
      return -2; // can not find solution
    }

    // algorith that uses find_steady_conditions....
    // goes up, must start from velocity set to mon takeoff speed.
    void easy_ride (double min_lift) {      
      double pitch = 0;
      double min_drag = 10000.0;
      double step = 2;
      for (int count = 0; count < 10000; count++) {
        trace("velocity: " + velocity + " step: " + step);
        steady_flight_at_given_speed(5, 0);
        double total_drag = total_drag();
        double total_lift = total_lift();
        if (Math.abs(step) < 0.01) break;
        if (//(step < 0 && velocity+step <= 0) ||
            (step > 0 && velocity >= 70) ||
            total_lift < load ||
            total_drag > min_drag) { // can't pivot because of local max of drag before taking off.... 
          velocity -= step;
          step *= 0.5;
        } else {
          velocity += step;
          min_drag = total_drag;
        }
      }
      make_cruising_info(min_lift, min_drag, velocity); 
      System.out.println("\nDone!\n----------------\n" + cruising_info);
    }

    // algorith that uses find_steady_conditions....
    // goes down, so start speed is fast
    void easy_ride_from_70kmh_down (double min_lift) {      
      double start_speed = 70; // kmh
      double pitch = 0;
      double min_drag = 10000.0;
      double step = -10;
      velocity = start_speed;
      for (int count = 0; count < 10000; count++) {
        trace("velocity: " + velocity + " step: " + step);
        steady_flight_at_given_speed(5, 0);
        double total_drag = total_drag();
        double total_lift = total_lift();
        if (Math.abs(step) < 0.01) break;
        if ((step < 0 && velocity+step <= 0) ||
            // (step > 0 && velocity >= 70) ||
            total_lift < load ||
            total_drag > min_drag) { // can't pivot because of local max of drag before taking off.... 
          velocity -= step;
          step *= 0.5;
        } else {
          velocity += step;
          min_drag = total_drag;
        }
      }
      make_cruising_info(min_lift, min_drag, velocity); 
      System.out.println("\nDone!\n----------------\n" + cruising_info);
    }


    // start speed must be >= than takeoff speed
    void easy_ride_old (double min_lift, double start_speed) {      
      double pitch = 0;
      double min_drag = 10000.0;
      double min_drag_speed = -1;
      for (double speed = start_speed; speed < v_max; speed += 0.5) {
        velocity = speed;
        trace("velocity: " + velocity);
        for (pitch = 0; pitch <= ang_max; pitch += 0.1) {
          craft_pitch = pitch;
          //computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
          // if (total_drag() >= max_drag) {
          //   trace("warning speed: " + speed + " pitch: " + pitch + " lift " + total_lift());
          //   return;
          // }
          if (total_lift() >= min_lift) {
            double drag = total_drag();
            if (min_drag > drag) {
              min_drag = drag;
              min_drag_speed = velocity;
              break;
            } else {
              // drag goes up now. can be done now, but
              // as a heuristic, now try from the upepr limit, and then find range:
              max_speed(min_lift, min_drag + 2, false);
              if (velocity < min_drag_speed) {
                // something went wrong (iteration stepped over the sweets spot etc)
                // so we take min_lift, min_drag, and min_drag_speed  as is
                make_cruising_info(min_lift, min_drag, min_drag_speed); 
                System.out.println("\nDone!\n----------------\n" + cruising_info);
              } else {
                // done, report with average of min_drag_speed and velocity
                make_cruising_info(min_lift, min_drag+1, (min_drag_speed+velocity)/2); 
                trace("done with refinement! ");
                System.out.println("\nDone!\n----------------\n" + cruising_info);
              }
              return;
            }
          }
        }
      }
    }


    void max_speed_slow (double min_lift, double max_drag, double v_start, double v_step, double pitch_start, double pitch_step) {
      double speed = v_start;
      double pitch;
      for (; speed >= 0; speed += v_step) {
        velocity = speed;
        for (pitch = pitch_start; pitch <= ang_max; pitch += pitch_step) {
          craft_pitch = pitch;
          //computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
          if (total_lift() >= min_lift) {
            // reached lift, what is the drag?
            if (total_drag() >= max_drag) {
              // stop increasing pitch, won't work. likely need less speed
              trace("speed: " + speed + " pitch: " + pitch);
              break;
            }
            make_max_speed_info(min_lift, max_drag, velocity);
            trace("done " + max_speed_info);
            return;
          }
        }
      }
    }

    void max_speed (double min_lift, double max_drag, boolean update_info) {
      // step 1: find lowest drag pitch at v=20kts
      velocity = kts_to_speed(20);
      find_aoa_of_min_drag();
      double pitch_of_min_drag = craft_pitch;
      trace("pitch_of_min_drag: " + pitch_of_min_drag);

      // step 2. with current (lowest drag) pitch, iterate speed up
      // until drag exceeds max_drag.
      double speed =  (total_drag() > max_drag) || total_lift() > min_lift
        ? kts_to_speed(5) // pretty arbitrary...
        : velocity;

      for (; speed < v_max; speed += 1) { 
        velocity = speed;
        //computeFlowAndRegenPlotAndAdjust();
        con.recomp_all_parts();
        if (total_drag() > max_drag) 
          break;
      }

      trace("velocity after step2: " + velocity);

      // // at min drag angle can not produce enough lift? unsolvable.
      // if (total_lift() < min_lift) {
      //   trace("can not solve 'fild max speed' task for given inputs.\nincrease drag input or decrease lift input");
      //   return;
      // }

      // step 3. pitch_of_min_drag correction for too much lift...
      if (total_lift() > min_lift) {
        // unsustainable, needs less pitch & speed...
        // reduce min_pitch until lift is OK
        while(pitch_of_min_drag > ang_min) {
          pitch_of_min_drag -= 0.05;
          craft_pitch = pitch_of_min_drag;
          //computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
          if (total_lift() <= min_lift)
            break;
        }
        trace("corrected pitch_of_min_drag: " + pitch_of_min_drag);
      }

      // step 4. from this speed, iterate speed down
      for (speed = velocity; speed >= 0; speed -= 1) {
        trace("speed: " + speed);
        double lift = 0;
        double drag = 0;
        double pitch = pitch_of_min_drag;
        velocity = speed;
        set_mast_aoa_for_given_drag(max_drag);
        for (; pitch < ang_max; pitch += 0.1) {
          craft_pitch = pitch;
          //computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
          lift = total_lift();
          drag = total_drag();
          if (drag >= max_drag) {
            // stop increasing pitch, won't work. 
            //likely need less speed
            trace("too much drag, break. speed: " + speed + " pitch: " + pitch);
            break;
          }
          if (lift >= min_lift) {
            trace("done ");
            if (update_info) {
              make_max_speed_info(min_lift, max_drag, velocity);
              System.out.println("\nDone!\n----------------\n" + max_speed_info);
            }
            return;
          } 
        }
        // here pitch is at max. what about lift?
        if (pitch >= ang_max && lift < min_lift) {
          trace("oops, can not be solved.\nincrease drag limit or decrease lift threshold!");
          return;
        }
      }
    }

    // cached state of last invocation of steady_flight_at_given_speed
    boolean steady_flight_at_given_speed___ok = false;

    // fly with given/current speed providing required lift, if not zero
    void steady_flight_at_given_speed (double step, double start_pitch) {
      // preamble: make sure inputs are in
      //computeFlowAndRegenPlotAndAdjust();

      //strut.aoa = 0.5; // ad hoc

      con.recomp_all_parts();

      double load = FoilBoard.this.load;
      rider_weight = load - BOARD_WEIGHT - RIG_WEIGHT - FOIL_WEIGHT;

      steady_flight_at_given_speed___ok = false; // so far util done

      craft_pitch = start_pitch;
      // double prev_pitch = -20; // we nned it only because pitch value gets rounded somewhere in recomp_all_parts...
      while (craft_pitch < ang_max && craft_pitch > ang_min) {
        //computeFlowAndRegenPlotAndAdjust();
        con.recomp_all_parts();
        double lift = total_lift();
        if (lift == load  // exact hit, done !(almost never happens, though)
            // happens due to rounding
            // || prev_pitch == craft_pitch
            ) 
          break;
        if (step > 0 && lift > load) { // done or pivot
          if (step < 0.0025) { 
            // done! flight is OK
            steady_flight_at_given_speed___ok = true;
            break;
          }
          step = -step/2; // shrink step & pivot
        } else if (step < 0 && lift < load) { 
          step = -step/2; // shrink step & pivot
        } // else keep going with current stepa
        // prev_pitch = craft_pitch;
        craft_pitch += step;
      }

      // expect small increse in drag as the result
      vpp.set_mast_aoa_for_given_drag(total_drag()); // (wing.drag+stab.drag);
      con.recomp_all_parts();

      // old linear increasin logic 
      //find_aoa_of_min_drag();
      //if (total_lift() > load_numeric) {
      //  // need to reduce AoA
      //  while (craft_pitch > ang_min) {
      //    craft_pitch -= 0.1;
      //    System.out.println("-- reducing... craft_pitch: " + craft_pitch);
      //    computeFlowAndRegenPlotAndAdjust();
      //    con.recomp_all_parts();
      //    if (total_lift() <= load_numeric) 
      //      break; // done
      //  }                      
      //}
      //else if (total_lift() < load_numeric) {
      //  // need to increase AoA
      //  while (craft_pitch < ang_max) {
      //    craft_pitch += 0.1;
      //    System.out.println("-- increasing... craft_pitch: " + craft_pitch);
      //    computeFlowAndRegenPlotAndAdjust();
      //    con.recomp_all_parts();
      //    if (total_lift() >= load_numeric) 
      //      break; // done
      //  }                      
      //}

    }

    
  } // class VPP

  class Controls extends Panel {
    FoilBoard app;
    JLabel l1,l2,blank,pitchMomentJLabel,liftOverDrag,reynoldsJLabel,FSlabel, lbl_cg_pos;
    double cg_pos, cg_pos_board_level;
    JComboBox outch,dragOutputCh,untch;

    JTextField outlft_wing, outlft_stab, outlft_strut, outlft_fuse;
    JTextField outDrag_wing, outDrag_stab, outDrag_strut, outDrag_fuse;
    JTextField outLD_wing,outLD_stab,outLD_strut,outLD_fuse;
    JTextField outReynolds_wing, outReynolds_stab, outReynolds_strut, outReynolds_fuse;

    JTextField[] outlft_arr, outDrag_arr, outLD_arr, outReynolds_arr;

    JTextField outTotalLift, outTotalDrag, outCGPosition, outPower, outTotalLDRatio;

    JTextField outMoment; // add mements????

    Button bt3,ibt_flight,ibt_shape,ibt_size,ibt_sel_plot,ibt_analysis, ibt_env;
    Button[] all_inputs;
    Button all_outputs[];
    ActionListener bt_wing_al, ibt_flight_al, bt_stab_al, bt_html_al;
    boolean html_render = true;

    void switch_to_part (Part p) {
      if (p == null) return;
      if (current_part != null)
        current_part.save_state();

      double conv = 1;
      current_part = p;
      // setFoil(p.foil);
      // chord = p.chord / conv;
      // span_old = span =  p.span  / conv;
      // arold = area = span * chord;
      //aspect_rat = span/chord;
      //current_part.spanfac = (int)(2.0*fact*aspect_rat*.3535);

      // current_part.thickness = p.thickness;
      // current_part.thickness/25 = current_part.thickness/25;
      // current_part.camber = p.camber;
      // current_part.camber/25 = current_part.camber / 25.0;
      //current_part.aoa = p.aoa;

      // current_part.t_Cl = p.t_Cl;
      // current_part.t_Cd = p.t_Cd;
      // current_part.cl = p.cl;
      //done current_part.cm = p.cm;
      // current_part.Ci_eff = p.Ci_eff;

      solver.load_stall_model_cache(p.foil);

      computeFlow(); 
      loadOutPanel();

      if (can_do_gui_updates) 
        in.load_current_panel();

    }

    int recom_all_parts_reentry_count = 0;
    void recomp_all_parts () {
      if (recom_all_parts_reentry_count > 3) {
         System.out.println("-- too deep recursive entry into recomp_all_parts, returning; ");
        // new Exception("=== see stack ============).printStackTrace(System.out);
        return;
      }
      recom_all_parts_reentry_count++;

      Part curr_pt = current_part;

      switch_to_part(stab);
      switch_to_part(fuse);
      switch_to_part(wing);
      switch_to_part(strut);

      // must do before switch_to_part(current_part);
      // so that updateTotals is triggered
      recom_all_parts_reentry_count--;  
      switch_to_part(curr_pt);

      if (can_do_gui_updates) out.plot.loadPlot();
    }

    JLabel addJLabel (String text, Color fg, Color bg, int align) {
      JLabel lb = new JLabel(text, align);
      if (fg != null) lb.setForeground(fg);
      if (bg != null) lb.setBackground(bg);
      add(lb);
      return lb;
    }

    JTextField addOutput () {
      JTextField out = new JTextField("12.5",5);
      out.setBackground(color_very_dark);
      out.setForeground(Color.yellow);
      out.setEditable(false);
      add(out);
      return out;
    }

    void outlft_setText (String text) {
      JTextField tf;
      if (current_part == wing) tf = outlft_wing;
      else if (current_part == stab) tf = outlft_stab;
      else if (current_part == strut) tf = outlft_strut;
      else tf = outlft_fuse;
      tf.setText(text);
      for (JTextField f : outlft_arr) f.setForeground(f == tf ? Color.YELLOW : Color.WHITE);
    }
   
    void outDrag_setText (String text) {
      JTextField tf;
      if (current_part == wing) tf = outDrag_wing;
      else if (current_part == stab) tf = outDrag_stab;
      else if (current_part == strut) tf = outDrag_strut;
      else tf = outDrag_fuse;
      tf.setText(text);
      for (JTextField f : outDrag_arr) f.setForeground(f == tf ? Color.YELLOW : Color.WHITE);
    }

    void outLD_setText (String text) {
      JTextField tf;
      if (current_part == wing) tf = outLD_wing;
      else if (current_part == stab) tf = outLD_stab;
      else if (current_part == strut) tf = outLD_strut;
      else tf = outLD_fuse;
      tf.setText(text);
      for (JTextField f : outLD_arr) f.setForeground(f == tf ? Color.YELLOW : Color.WHITE);
    }
   
    void outReynolds_setText (String text) {
      JTextField tf;
      if (current_part == wing) tf = outReynolds_wing;
      else if (current_part == stab) tf = outReynolds_stab;
      else if (current_part == strut) tf = outReynolds_strut;
      else tf = outReynolds_fuse;
      tf.setText(text);
      for (JTextField f : outReynolds_arr) f.setForeground(f == tf ? Color.YELLOW : Color.WHITE);
    }

    void bt_action_switch_to_part (Button bt, Part part) {
      if (part_button != bt) {
        part_button.setBackground(Color.white);
        part_button = bt;
        part_button.setBackground(Color.yellow);
        switch_to_part(part);
        current_part.foil.adjust_foil_shape_in_tab();
        //ttbt in.load_current_panel();
        //ttbt out.plot.loadPlot();
      }    
    }

    int mast_aoa_rolodex = 0;
   
    Controls (FoilBoard target) { 
      app = target;
      setLayout(new GridLayout(9,/*ignored!*/0,5,5));

      // l1 = new JLabel("Output", JLabel.RIGHT);
      // l1.setForeground(Color.red);
      // l2 = new JLabel("Input", JLabel.CENTER);
      // l2.setForeground(Color.blue);



      bt3 = new Button("Help");
      bt3.setBackground(Color.red);
      bt3.setForeground(Color.white);
      bt3.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            helpPopUp();
          }});


      untch = new JComboBox();
      untch.setBackground(Color.white);
      untch.setForeground(color_very_dark);
      untch.addItem("Imperial");
      untch.addItem("Metric scientific");
      untch.addItem("Metric kg/kmh");
      untch.addItem("Naval");
      untch.addItem("Imperial ft");
      untch.setSelectedIndex(0);
      untch.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            display_units = untch.getSelectedIndex();
            recomp_all_parts();
            in.load_current_panel();
            can_do_gui_updates = true;
          }
        });

      outch = new JComboBox();
      outch.setBackground(Color.white);
      outch.setForeground(color_very_dark);
      outch.addItem("Lift ");
      outch.addItem("  Cl ");
      outch.addItem("Total Lift");
      outch.setSelectedIndex(0);
      outch.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            lftout = outch.getSelectedIndex();
            recomp_all_parts();
            can_do_gui_updates = true;
          }
        });

      dragOutputCh = new JComboBox();
      dragOutputCh.setBackground(Color.white);
      dragOutputCh.setForeground(color_very_dark);
      dragOutputCh.addItem("Drag");
      dragOutputCh.addItem(" Cd ");
      dragOutputCh.setSelectedIndex(0);
      dragOutputCh.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            dragOut = dragOutputCh.getSelectedIndex();
            recomp_all_parts();
            can_do_gui_updates = true;
          }
        });

      pitchMomentJLabel = new JLabel("Cm",JLabel.RIGHT);
 
      liftOverDrag = new JLabel("L/D ratio",JLabel.RIGHT);
      liftOverDrag.setForeground(color_very_dark);

      reynoldsJLabel = new JLabel("Reynolds #", JLabel.RIGHT);
      reynoldsJLabel.setForeground(color_very_dark);


      // row 1 


      addJLabel("Kite/Wind Foil", Color.red, null, JLabel.RIGHT);
      addJLabel("Simulator v1.0", Color.red, null, JLabel.LEFT);
      // doe snot fit in 1
      // addJLabel(t_foil_name, null, null, JLabel.RIGHT);
      addJLabel(make_name, null, null, JLabel.RIGHT);
      addJLabel(model_name, null, null, JLabel.RIGHT);
      addJLabel(year_etc, null, null, JLabel.RIGHT);


      // add(l2);
      // add(new JLabel("Student ", JLabel.RIGHT));
      // add(new JLabel(" Version 1.5b", JLabel.LEFT));
      // add(l1);

      // row 2
      {

        addJLabel("Foil part:", null, null, JLabel.RIGHT);
        
        final Button bt_strut = new_button("Mast");
        part_button = bt_strut;
        bt_strut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
              bt_action_switch_to_part(bt_strut, strut);
              can_do_gui_updates = true;
            }
          });

        bt_wing = part_button = new Button ("Wing");
        bt_wing.setBackground(Color.yellow);
        bt_wing.setForeground(Color.blue);
        bt_wing.addActionListener(bt_wing_al = new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
              bt_action_switch_to_part(bt_wing, wing);
              can_do_gui_updates = true;
            }
          });
        add(bt_wing);

        bt_stab = new_button("Stab");
        bt_stab.addActionListener(bt_stab_al = new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
              bt_action_switch_to_part(bt_stab, stab);
              can_do_gui_updates = true;
            }
          });
        add(bt_stab);

        final Button bt_fuse = new_button("Fuse");
        bt_fuse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
              bt_action_switch_to_part(bt_fuse, fuse);
              can_do_gui_updates = true;
            }
          });

        add(bt_strut);
        add(bt_fuse);

      }


      add(outch);
      outlft_wing = addOutput();
      outlft_stab = addOutput();
      outlft_strut = addOutput();
      outlft_fuse = addOutput();
      outlft_arr = new JTextField[]{outlft_wing, outlft_stab, outlft_strut, outlft_fuse};

      add(dragOutputCh);
      outDrag_wing = addOutput();
      outDrag_stab = addOutput();
      outDrag_strut = addOutput();
      outDrag_fuse  = addOutput();
      outDrag_arr = new JTextField[]{outDrag_wing, outDrag_stab, outDrag_strut, outDrag_fuse};

      // outlft_strut.addMouseListener(new java.awt.event.MouseAdapter() {
      //     @Override
      //     public void mouseClicked(MouseEvent e) {
      //       System.out.println("-- mouseClicked: " + e);
      //       if (++mast_aoa_rolodex % 3 == 0)
      //         strut.aoa = 0;
      //       else {
      //         boolean saved = vpp.set_mast_aoa_for_given_drag_auto;
      //         vpp.set_mast_aoa_for_given_drag_auto = true;                
      //         double mast_load_force = (mast_aoa_rolodex % 3) * total_drag();
      //         System.out.println("-- mast_load_force: " + mast_load_force);
      //         vpp.set_mast_aoa_for_given_drag(mast_load_force);
      //         vpp.steady_flight_at_given_speed(5, 0);
      //         vpp.set_mast_aoa_for_given_drag_auto = saved;
      //       }
      //       vpp.steady_flight_at_given_speed(5, 0);
      //     }
      //   });

      add(liftOverDrag);
      outLD_wing = addOutput();
      outLD_stab = addOutput();
      outLD_strut = addOutput();
      outLD_fuse = addOutput();
      outLD_arr  = new JTextField[]{outLD_wing,outLD_stab,outLD_strut,outLD_fuse};

      // row 6
      add(reynoldsJLabel);
      outReynolds_wing = addOutput();
      outReynolds_stab = addOutput();
      outReynolds_strut = addOutput();
      outReynolds_fuse = addOutput();
      outReynolds_arr  = new JTextField[]{outReynolds_wing, outReynolds_stab, outReynolds_strut, outReynolds_fuse};

      
      // row 7. Panel tab coplot_trace_countols. output coplot_trace_countols

      addJLabel("Units: ", null, null, JLabel.RIGHT);
      add(untch);
      add(bt3);

      boolean want_matte_border = false;

      if (want_matte_border) {
        JLabel jlbl = new JLabel("Total Lift", JLabel.RIGHT);
        jlbl.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 0, color_very_dark));
        add(jlbl);
      } else
        addJLabel("Total Lift", null, null, JLabel.RIGHT);

      outTotalLift = addOutput();


      // row 8
      add(new JLabel("Total L/D"));
      outTotalLDRatio = addOutput();
      add(new JLabel(""));

      if (want_matte_border) {
        JLabel jlbl = new JLabel("Total Drag", JLabel.RIGHT);
        jlbl.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 0, color_very_dark));
        add(jlbl);
      } else
        addJLabel("Total Drag", null, null, JLabel.RIGHT);

      outTotalDrag = addOutput();

      // row 9 
      add(new JLabel("Power"));
      outPower = addOutput();
      add(new JLabel(""));

      lbl_cg_pos = addJLabel("CG to Mast LE", null, null, JLabel.RIGHT);
      outCGPosition = addOutput();

      // row 8
      //add(ibt_env);
      //add(ibt_sel_plot);
      //add(bt_probe);
      //add(bt_geom);
      //add(bt_data);

      // row 9
      // add(ibt_analysis);
      // addJLabel("", null, null, JLabel.RIGHT);
      // add(bt_gages);
      // add(bt_plot);
    }


  } // Con

  class In extends javax.swing.JTabbedPane {
    FoilBoard app;
    Flight flt;
    Shape shp;
    Size size;
    BallCylinderShape cylShape;
    PlotSelectorTab grf;
    Anl anl;
    Env env;
    Panel[] all;
    Panel current;

    public void addTab(Panel p, String title, String tip) {
      addTab(title, null, p, tip);
      if (p.getName() == null) p.setName(title);
      // tip too?
    }

    In (FoilBoard target) { 
      app = target;
      layin = new CardLayout();
      //setLayout(layin);

      flt = new Flight(app);
      shp = new Shape(app);
      size = new Size(app);       
      cylShape = new BallCylinderShape(app);
      grf = new PlotSelectorTab(app);
      anl = new Anl(app);
      env = new Env(app);

      all = new Panel[] {
          flt,
            shp,
            size,  
            cylShape,
            grf,
            anl,
            env
            };

      addTab(flt, "Flight", "Flight Panel");
      addTab(shp, "Shape", "foil-shape-panel");
      addTab(size, "Size", "Size Panel");
      addTab(grf, "Choose Plot", "Select Plot Panel");
      addTab(anl, "Options", "Settings Panel");

      // not very useful for Hydrofoiling board...
      if (use_cylinder_shapes)
        addTab(cylShape, "Ball", "cylinder-or-ball-shape-panel");

      // not really useful for Hydrofoiling board... for now, include
      // only if cylinders or Foilsim foils are included
      if (use_cylinder_shapes || use_foilsim_foils)
        addTab(env, "Env", "Environment Panel");

      // can be used to attach aux logic here..
      this.addChangeListener(new javax.swing.event.ChangeListener() {
                    @Override
                    public void stateChanged(javax.swing.event.ChangeEvent e) {
                      In pane = (In) e.getSource();
                      //System.out.println("Selected paneNo : " + pane.getSelectedIndex());
                    } });
    }


    void set_current () {
      current = (Panel)getSelectedComponent();
    }
    
    void load_current_panel () {
      set_current();
      current.loadPanel();
    }
    
    void switch_to (String tab_name, String panel_name) {
      int idx = indexOfTab(tab_name);
      setSelectedIndex(idx);
      set_current();
      can_do_gui_updates = true;
    }

    void toggle_shp_tab () {
      if (in.indexOfComponent(cylShape) > -1) 
        in.remove(cylShape);
      int idx = indexOfComponent(shp);
      if (idx < 0) 
        add(shp, idx=1); // right after Flight tab
    }
    void toggle_cyl_tab () {
      if (in.indexOfComponent(shp) > -1) 
        in.remove(shp);
      int idx = indexOfComponent(cylShape);
      if (idx < 0) 
        add(cylShape, idx=1); // right after Flight tab
    }
    
    void switch_to (Component c) {
      int idx = indexOfComponent(c);
      if (idx < 0) 
        add(c, idx=1); // right after Flight tab
      setSelectedIndex(idx);
      set_current();
    }

    public void update_state () {
      for (Button b : shp.old_style_buttons) b.setBackground(Color.WHITE);

      if (current_part.foil == FOIL_CYLINDER || current_part.foil == FOIL_BALL) {
        current_part.aoa = 0.0;
        in.toggle_cyl_tab();
        in.switch_to(cylShape);
        if (current_part.foil == FOIL_CYLINDER) {
          // in.anl.cbt1.setBackground(Color.white);
          // in.anl.cbt2.setBackground(Color.white);
          // in.anl.cbt3.setBackground(Color.white);
        } else if (current_part.foil == FOIL_BALL) {
          current_part.span = radius;
          current_part.area = 3.1415926*radius*radius;
          if (viewflg != VIEW_EDGE) viewflg = VIEW_EDGE;
          bdragflag = 1;
          // in.anl.cbt1.setBackground(Color.yellow);
          // in.anl.cbt2.setBackground(Color.white);
          // in.anl.cbt3.setBackground(Color.white);
        } 
      } else {
        in.toggle_shp_tab();
        in.switch_to(shp);

        if (current_part.foil == FOIL_FLAT_PLATE) {
          current_part.thickness = thk_min;
        } 
        in.shp.loadPanel(); // takes care of the 3 boxes and 3 slider pos
      } 
    
      if (!foil_is_cylinder_or_ball(current_part.foil)) {
        //layplt.show(in.grf.l, "first");
        // in.anl.bt7.setBackground(Color.yellow);
        // in.anl.bt8.setBackground(Color.white);
        // in.anl.cbt1.setBackground(Color.white);
        // in.anl.cbt2.setBackground(Color.white);
        // in.anl.cbt3.setBackground(Color.white);
      } else {
        //layplt.show(in.grf.l, "second");
        //in.anl.bt7.setBackground(Color.white);
        //in.anl.bt8.setBackground(Color.yellow);
      }

      //in.cylShape.rightPanel.shape_choice.setSelectedIndex(foil.id);
      //layout.show(out, "first");
      // con.bt_plot.setBackground(Color.yellow);
      // con.bt_probe.setBackground(Color.white);
      // con.bt_gages.setBackground(Color.white);
      // con.bt_geom.setBackground(Color.white);
      // con.bt_data.setBackground(Color.white);
      // plot_type = 0;

      shp.recompute();
    }

    class InputPanel extends Panel {
      public void paint (Graphics g) {
        loadPanel();
        super.paint(g);
      }
    }
 
    class Flight extends InputPanel {
      FoilBoard app;
      boolean autobalance = true;

      class Name extends JLabel { 
        Name(String text) { super(text, JLabel.RIGHT); }
        Name(String text, int align) { super(text, align); }
      }

      // Example: Speed,km/h [ 20] <-----x---->
      class NameBoxBar extends InputPanel{ 
        Name name;
        JTextField box;
        JScrollBar bar;
        NameBoxBar(Flight ft, String text, String val, double min, double max) { 
          name = new Name(text);
          box = new JTextField(val, 5);
          String[] input = val.trim().split("\\s+");
          double val_double = Double.parseDouble(input[0]);
          if (val_double < min) val_double = min;
          else if (val_double > max) val_double = max;
          int pos = (int) (((val_double - min)/(max- min))*1000.);
          bar = new JScrollBar(JScrollBar.HORIZONTAL, pos ,10, 0, 1000);
          Panel left = new Panel(new GridLayout(1,2,2,2));
          left.add(name);
          left.add(box);
          Panel pair = new Panel(new GridLayout(1,2,0,0)); // ?? 2,2 ??
          pair.add(left);
          pair.add(bar);
          ft.add(pair);
        }
        NameBoxBar(Flight ft, String text, String prop_name, String dflt, double min, double max) { 
          this(ft, text, getParamOrProp(prop_name, dflt), min, max);
        }
      }

      // Example:  Lift< [  735] Drag< [  85] {Find Lowest Takeoff Speed}
      class NameBoxNameBoxButton extends Panel{ 
        Name name1, name2;
        JTextField box1, box2;
        Button button;
        Panel constraints, pair;
        NameBoxNameBoxButton (Flight ft, 
                              String text1, String prop1, String dflt1, 
                              String text2, String prop2, String dflt2, 
                              String button_text, ActionListener action) { 
          name1 = new Name(" @ " + text1);
          box1 = new JTextField(getParamOrProp(prop1, dflt1), 5);

          name2 = new Name(text2);
          box2 = new JTextField(getParamOrProp(prop2, dflt2), 5);

          button = new Button(button_text);
          button.addActionListener(action);
          constraints = new Panel(new GridLayout(1,4,2,2));
          constraints.add(name1);
          constraints.add(box1);
          constraints.add(name2);
          constraints.add(box2);
          pair = new Panel(new GridLayout(1,2)); // ?? 2,2 ??
          pair.add(button);
          pair.add(constraints);
          ft.add(pair);
        }
      }

      JTextField f1,fAoA;

      JScrollBar s1, sAoA;
      JLabel constraints_label;

      JTextField tf_tkoff_min_lift, tf_tkoff_max_drag;
      JTextField tf_cruise_min_lift, tf_cruise_starting_speed;
      JTextField tf_race_min_lift, tf_race_max_drag;

      int constr_tkoff_min_lift, constr_tkoff_max_drag;
      int constr_cruise_min_lift, constr_cruise_starting_speed;
      int constr_race_min_lift, constr_race_max_drag;

      NameBoxBar speed_ctrl, pitch_ctrl, alt_ctrl, load_ctrl, mfp_ctrl;

      boolean on_load = false;

      int set_to_units = METRIC;

      // Flight.loadPanel
      @Override
      public void loadPanel () {   // load the flight panel gauges
        if (set_to_units != display_units) {
          switch (display_units) {
          case IMPERIAL:
            speed_ctrl.name.setText("Speed mph");
            load_ctrl.name.setText("Load lb");
            break;
          case NAVAL:
            speed_ctrl.name.setText("Speed kt");
            load_ctrl.name.setText("Load lb");
            break;
          case METRIC:
            speed_ctrl.name.setText("Speed m/s");
            load_ctrl.name.setText("Load N");
            break;
          case METRIC_2:
            speed_ctrl.name.setText("Speed km/h");
            load_ctrl.name.setText("Load kg");
            break;
          }
          constraints_label.setText("    \u2193    Constraints, in "+current_display_force_unit_string()+"    \u2193");
          
          // need to adjust the VPP constraint boxes.
          tf_tkoff_min_lift.setText(make_force_info_in_display_units((double)constr_tkoff_min_lift, false));
          tf_tkoff_max_drag.setText(make_force_info_in_display_units((double)constr_tkoff_max_drag, false));
          tf_cruise_min_lift.setText(make_force_info_in_display_units((double)constr_cruise_min_lift, false));
          tf_race_min_lift.setText(make_force_info_in_display_units((double)constr_race_min_lift, false));
          tf_race_max_drag.setText(make_force_info_in_display_units((double)constr_race_max_drag, false));

          set_to_units = display_units;

        }
        
        on_load = true;
        // calling setValue on the bars generates events that set these text boxes...
        // howeverm value *will* be rounded, then to 1000 steps, so must use on_load
        speed_ctrl.bar.setValue((int)(((velocity- v_min)/(v_max-v_min))*1000.));
        pitch_ctrl.bar.setValue((int) (((craft_pitch - ang_min)/(ang_max-ang_min))*1000.));
        alt_ctrl.bar.setValue((int) (((alt - alt_min)/(alt_max-alt_min))*1000.));
        load_ctrl.bar.setValue((int) (((load - load_min)/(load_max-load_min))*1000.));
        on_load = false;

        speed_ctrl.box.setText(make_speed_info_in_display_units(velocity, false));
        pitch_ctrl.box.setText(pprint(filter3(craft_pitch))); // was: filter5 ...
        alt_ctrl.box.setText(pprint(filter1(alt)));
        load_ctrl.box.setText(make_force_info_in_display_units(load, false));
        if (mfp_ctrl != null) mfp_ctrl.box.setText(pprint(filter1(100*mast_foot_pressure_k)));
        //load_ctrl.box.setText(make_force_info_in_display_units(vpp.steady_flight_at_given_speed___load, false));

        // todo: other knobs

        out.plot.loadPlot();
      }

      // later: bind it to others sliders (load?)
      void find_steady_conditions () {
        // System.out.println("-- find_steady_conditions: " + can_do_gui_updates);
        // double min_lift = parse_force_constraint(tf_tkoff_min_lift, "TKL", "735");
        double max_drag = parse_force_constraint(tf_tkoff_max_drag, "TKD", craft_pitch == WINDFOIL ? "85" : "120");
        boolean saved_flag = can_do_gui_updates;
        can_do_gui_updates = false;
        vpp.steady_flight_at_given_speed(5, 0);
        can_do_gui_updates = saved_flag;
        con.recomp_all_parts();
        loadPanel();
        if (total_drag() > max_drag) 
          con.outTotalDrag.setForeground(Color.red);
        if (total_lift() < load) 
          con.outTotalLift.setForeground(Color.red);
      }

      Flight (FoilBoard target) {
        int i1,i2, iAoA;

        app = target;

        int rows = 0;
        // panel under profile drawing - 10 rows

        // row 1
        rows++;
        speed_ctrl = new NameBoxBar(this, "Speed m/s",  "20.0", v_min, v_max);
        speed_ctrl.bar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              if (on_load) return;
              int i1 = evt.getValue();
              float new_velocity = filter3(i1 * (v_max - v_min)/ 1000. + v_min);
              if (new_velocity == velocity) return;
              velocity = new_velocity;
              // look as loadPanel takes care
              // f1.setText(String.valueOf(velocity));

              if (autobalance)
                find_steady_conditions();
              else 
                con.recomp_all_parts();

              //  set limits on spin
              if (foil_is_cylinder_or_ball(current_part.foil)) cylShape.setLims();

              //?speed_kts_mph_kmh_ms_info = make_speed_kts_mph_kmh_ms_info(velocity);
            }});
        speed_ctrl.box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               double speed = Double.valueOf(f1.getText()).doubleValue();
               speed = speed_input_in_display_units_to_kmh(speed, display_units);
               speed = filter1or3(speed);
               if (speed != velocity) {
                 velocity = limit(v_min, speed, v_max);
               speed_ctrl.box.setText(String.valueOf(speed));

               // i1 = (int) (((speed - v_min)/(v_max-v_min))*1000.);
               // s1.setValue(i1);

               //  set limits on spin
               if (foil_is_cylinder_or_ball(current_part.foil)) cylShape.setLims();

               if (autobalance)
                 find_steady_conditions();
               else 
                 con.recomp_all_parts();
               //?speed_kts_mph_kmh_ms_info = make_speed_kts_mph_kmh_ms_info(velocity);
             }
            } });
        

        f1 = speed_ctrl.box; // legacy..
        s1 = speed_ctrl.bar; // legacy..

        // row 3
        rows++;
        alt_ctrl = new NameBoxBar(this, "Altitude %", "70", alt_min, alt_max);
        alt_ctrl.bar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              if (on_load) return;
              float new_val = filter3(alt_ctrl.bar.getValue() * (alt_max - alt_min)/ 1000. + alt_min);
              if (new_val == alt) return;
              alt = new_val;
              //con.recomp_all_parts();
              //? computeFlowAndRegenPlot();
              if (autobalance)
                find_steady_conditions();
              else 
                con.recomp_all_parts();
            }});
        alt_ctrl.box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              double new_val = Double.valueOf(alt_ctrl.box.getText()).doubleValue();
              if (new_val != alt) {
                alt = new_val;
                //alt_ctrl.bar.setValue((int) (((alt - alt_min)/(alt_max-alt_min))*1000.));

                if (autobalance)
                  find_steady_conditions();
                else 
                  con.recomp_all_parts();

                //?speed_kts_mph_kmh_ms_info = make_speed_kts_mph_kmh_ms_info(velocity);
              }
            }});

        // row 4
        rows++;
        load_ctrl = new NameBoxBar(this, "Load N", "CRL", 
                                   craft_type == WINDFOIL ? "735" : "650", 
                                   load_min, load_max);
        load = Double.parseDouble(load_ctrl.box.getText());
        load_ctrl.bar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              if (on_load) return;

              double new_val = filter1(load_ctrl.bar.getValue() * (load_max - load_min)/ 1000.);
              new_val = limit(load_min, new_val, load_max);
              if (new_val == load) return;
              load = new_val;
              if (autobalance)
                find_steady_conditions();
              else 
                con.recomp_all_parts();
            }});
        load_ctrl.box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              double new_val = Double.valueOf(load_ctrl.box.getText()).doubleValue();
              new_val = filter1(force_input_in_display_units_to_n(new_val, display_units));
              new_val = limit(load_min, new_val, load_max);
              if (new_val != load) {
                load = new_val;
                if (autobalance)
                  find_steady_conditions();
                else 
                  con.recomp_all_parts();
                //?speed_kts_mph_kmh_ms_info = make_speed_kts_mph_kmh_ms_info(velocity);
              }
            }}); 

        // row 2
        rows++;
        pitch_ctrl = new NameBoxBar(this, "Craft Pitch deg", "0.0", ang_min, ang_max);
        pitch_ctrl.bar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              if (on_load) return;
              float new_pitch = filter3(evt.getValue() * (ang_max - ang_min)/ 1000. + ang_min);
              if (new_pitch == craft_pitch) return;

              craft_pitch = new_pitch;

              // look as loadPanel takes care
              // fAoA.setText(String.valueOf(new_pitch));
              // we do not autobalance
              con.recomp_all_parts();
              //? computeFlowAndRegenPlot();
            }});
        pitch_ctrl.box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            double new_pitch = Double.valueOf(pitch_ctrl.box.getText()).doubleValue();
            System.out.println("-- new_pitch: " + new_pitch);
            if (new_pitch != craft_pitch) {
              craft_pitch = new_pitch;
              // sAoA.setValue((int) (((craft_pitch - ang_min)/(ang_max-ang_min))*1000.));
              con.recomp_all_parts();
              //?speed_kts_mph_kmh_ms_info = make_speed_kts_mph_kmh_ms_info(velocity);
            }
            }});

        sAoA = pitch_ctrl.bar;
        fAoA = pitch_ctrl.box;


        Panel p;
        JCheckBox chb;

        // row 6
        rows++;
        add(p = new Panel(new GridLayout(1,2,0,0)));
        Button b = new Button ("Pitch of balanced flight"); // old: ("Steady Flight @ Given Load");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              find_steady_conditions();
              can_do_gui_updates = true;
            }});
        p.add(b); 
        // p.add(p = new Panel(new GridLayout(1,2,0,0)));
        p.add(chb = new JCheckBox("Autoadjust Pitch for Equilibrium", true));
        chb.setToolTipText("Automatically adjust the craft pitch for level flight");
        chb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
              autobalance = e.getStateChange() == ItemEvent.SELECTED;
              System.out.println("-- pitch auto adjustment for required load is " + (autobalance ? "ON" : "OFF"));
            }
          });

        
        // p.add(new JLabel("trace"));
        // row 5
        rows++;

        if (false) { // want MFP control?
          mfp_ctrl = new NameBoxBar(this, "MFP", "MFP", "0", 0, 35);
          if (Double.parseDouble(getParamOrProp("MFP","0")) < 0 || craft_type == KITEFOIL) {
            mast_foot_pressure_k = 0;
            mfp_ctrl.box.enable(false);
            mfp_ctrl.bar.enable(false);
            mfp_ctrl.name.enable(false);
          }
          mfp_ctrl.bar.addAdjustmentListener(new AdjustmentListener() {
              public void adjustmentValueChanged(AdjustmentEvent evt) {
                if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                if (on_load) return;

                float new_mfp = filter3(0.01*evt.getValue() * (35 - 0)/ 1000.);
                if (new_mfp == mast_foot_pressure_k) return;
                mast_foot_pressure_k = new_mfp;
                con.recomp_all_parts();
                //? computeFlowAndRegenPlot();
              }});
        } else {
          
          add(p = new Panel(new GridLayout(1,2,0,0)));
          p.add(new JLabel(""));
          p.add(chb = new JCheckBox("Autoload Strut/Mast", false));
          chb.setToolTipText("Automatically adjust foil mast/strut AoA to realistically load it accotding to current drag force");
          chb.addItemListener(new ItemListener() {
              public void itemStateChanged(ItemEvent e) {             
                vpp.set_mast_aoa_for_given_drag_auto  = e.getStateChange() == ItemEvent.SELECTED;
                System.out.println("-- mast autolading is " + (vpp.set_mast_aoa_for_given_drag_auto ? "ON" : "OFF"));
                if (!vpp.set_mast_aoa_for_given_drag_auto) strut.aoa = 0;
                vpp.set_mast_aoa_for_given_drag(total_drag());
                con.recomp_all_parts(); // just in case 
                vpp.set_mast_aoa_for_given_drag(total_drag());
                con.recomp_all_parts(); // just in case 
                //vpp.steady_flight_at_given_speed(5, 0);
              }
            });              

        }

        // row 7
        rows++;
        add(new JLabel("\u2015\u2015\u2015\u2015\u2015\u2015   VELOCITY PREDICTION MODULE (a.k.a. VPP) \u2015\u2015\u2015\u2015\u2015\u2015", JLabel.CENTER));

        p = new Panel(new GridLayout(1,2,0,0));
        p.add(new JLabel("    \u2193     Solvers     \u2193", JLabel.LEFT));
        p.add(constraints_label = new JLabel("    \u2193    Constraints, in "+current_display_force_unit_string()+"    \u2193", JLabel.LEFT));
        // row 8
        rows++;
        add(p);


        // row 9
        rows++;
        NameBoxNameBoxButton takeoff = 
          new NameBoxNameBoxButton(this, 
                                   "Lift >", "TKL", craft_type == WINDFOIL ? "735" : "600", 
                                   "Drag <", "TKD", craft_type == WINDFOIL ? "85" : "120",
                                   "Find Lowest TakeOff speed",
                                   new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                       double min_lift = (double)constr_tkoff_min_lift; // parse_force_constraint(tf_tkoff_min_lift, "TKL", "735");
                                       load = min_lift; // will do the trick???
                                       double max_drag = (double)constr_tkoff_max_drag; // parse_force_constraint(tf_tkoff_max_drag, "TKD", "85");
                                       alt = alt_min;
                                       loadPanel();
                                       can_do_gui_updates = false;
                                       vpp.find_min_takeoff_v(min_lift, max_drag);
                                       can_do_gui_updates = true;
                                       con.recomp_all_parts();
                                       loadPanel();
                                     }}); 

        tf_tkoff_min_lift = takeoff.box1;
        constr_tkoff_min_lift = filter0(Double.parseDouble(tf_tkoff_min_lift.getText()));
        tf_tkoff_min_lift.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              constr_tkoff_min_lift = filter0(parse_force_constraint(tf_tkoff_min_lift, "", ""));
            }});

        
        tf_tkoff_max_drag = takeoff.box2;
        constr_tkoff_max_drag = filter0(Double.parseDouble(tf_tkoff_max_drag.getText()));
        tf_tkoff_max_drag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              constr_tkoff_max_drag = filter0(parse_force_constraint(tf_tkoff_max_drag, "", ""));
            }});

        // row 10
        rows++;
        NameBoxNameBoxButton cruise = 
          new NameBoxNameBoxButton(this, 
                                   "Lift >", "CRL", craft_type == WINDFOIL ? "735" : "650",
                                   "Speed >=", 
                                   "CRS", "5",
                                   "Find Speed of lesser Drag",
                                   new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                       double min_lift = (double)constr_cruise_min_lift; // parse_force_constraint(tf_cruise_min_lift, "CRL", "735");
                                       load = min_lift; // will do the trick???
                                       double starting_speed = (double)constr_cruise_starting_speed; // parse_speed_constraint(tf_cruise_starting_speed, "CRS", "10 km/h");
                                       System.out.println("-- constr_cruise_starting_speed: " + constr_cruise_starting_speed);
                                       can_do_gui_updates = false;
                                       alt = 80;
                                       strut.aoa = 0;
                                       vpp.find_min_takeoff_v(min_lift, (double)constr_tkoff_max_drag); // find starting point
                                       vpp.easy_ride(min_lift); // for old: min_takeoff_speed > 0 ? min_takeoff_speed : constr_cruise_starting_speed);
                                       con.recomp_all_parts();
                                       vpp.set_mast_aoa_for_given_drag(total_drag()); // (wing.drag+stab.drag);
                                       can_do_gui_updates = true;
                                       con.recomp_all_parts();
                                       loadPanel();
                                     }});

        tf_cruise_min_lift = cruise.box1;
        constr_cruise_min_lift = filter0(Double.parseDouble(tf_cruise_min_lift.getText()));
        tf_cruise_min_lift.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              constr_cruise_min_lift = filter0(parse_force_constraint(tf_cruise_min_lift, "", ""));
            }});

        tf_cruise_starting_speed = cruise.box2;
        
        // for now...
        cruise.constraints.remove(tf_cruise_starting_speed);
        cruise.constraints.remove(cruise.name2);
        cruise.constraints.add(new Name(""));
        cruise.constraints.add(new Name(""));
        
        constr_cruise_starting_speed = filter0(Double.parseDouble(tf_cruise_starting_speed.getText()));
        

        // row 11
        rows++;
        NameBoxNameBoxButton race = 
          new NameBoxNameBoxButton(this, 
                                   "Lift >", "RSL", "735", 
                                   "Drag <", "RSD", "245", // 25 kg force
                                   "Find Max Possible Steady Speed",
                                   new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                       double min_lift = (double)constr_race_min_lift; // parse_force_constraint(tf_race_min_lift, "RSL", "735");
                                       load = min_lift; // will do the trick???
                                       double max_drag = (double)constr_race_max_drag; // parse_force_constraint(tf_race_max_drag, "RSD", "245");
                                       can_do_gui_updates = false;
                                       alt = 70;
                                       vpp.max_speed(min_lift, max_drag, true);
                                       can_do_gui_updates = true;
                                       con.recomp_all_parts();
                                       loadPanel();
                                     }});

        tf_race_min_lift = race.box1;
        constr_race_min_lift = filter0(Double.parseDouble(tf_race_min_lift.getText()));
        tf_race_min_lift.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              constr_race_min_lift = filter0(parse_force_constraint(tf_race_min_lift, "", ""));
            }});

        tf_race_max_drag = race.box2;
        constr_race_max_drag = filter0(Double.parseDouble(tf_race_max_drag.getText()));
        tf_race_max_drag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              constr_race_max_drag = filter0(parse_force_constraint(tf_race_max_drag, "", ""));
            }});


        Panel bottom = new Panel(new GridLayout(1,2,2,2));
        bottom.add(new JLabel(""));

        Panel buttons = new Panel(new GridLayout(1,2,2,10));

        bottom.add(buttons);
        b = new Button ("Min Drag Pitch");
        buttons.add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              vpp.find_aoa_of_min_drag();
              can_do_gui_updates = true;
            }});
        

        buttons.add(chb = new JCheckBox("Trace", false));
        chb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
              vpp.trace = e.getStateChange() == ItemEvent.SELECTED;
              System.out.println("-- trace: " + vpp.trace);
            }
          });

        // att: currently exclude these
        // // row 12
        // rows++;
        // add(bottom);

        // done!
        setLayout(new GridLayout(rows,1,2,2));
      }

      boolean separate_suffix (String val, JTextField tf, String suff) {
        if (val.endsWith(suff)) { 
          tf.setText(val.replace(suff, " " + suff));
          return true;
        }
        return false;
      }

      // no suffix mode
      double parse_force_constraint (JTextField tf, String prop_name, String dflt) {
        return force_input_in_display_units_to_n(tf, display_units);
      }

      // for convenience, allow user to enter units here
      // todo: respect current unit mode.
      double parse_force_constraint_with_suffix (JTextField tf, String prop_name, String dflt) {
        String[] input = tf.getText().trim().split("\\s+");
        switch (input.length) {
        case 0: 
          dflt = (dflt==null || 
                  dflt.equals("") || 
                  !dflt.matches("[+-\\.]?\\d*[\\.]?\\d*")) 
            ? "10" : dflt;
          tf.setText(getParamOrProp(prop_name, dflt));
          return parse_force_constraint(tf, prop_name, dflt);
        case 1:
          String s = input[0].toLowerCase();
          if (
              separate_suffix(s, tf, "kg")  ||
              separate_suffix(s, tf, "lb") || 
              separate_suffix(s, tf, "n") ||
              // odd ones            
              separate_suffix(s, tf, "kgf") ||
              separate_suffix(s, tf, "lbf") ||
              false)
            return parse_force_constraint(tf, prop_name, dflt);
          else {
            tf.setText(s + " " + "N");
            return parse_force_constraint(tf, prop_name, dflt);
          }
            
        default:
          double val = Double.parseDouble(input[0].toLowerCase());
          String unit = input[1].toLowerCase();
          if (unit.startsWith("n")) return val;
          else if (unit.startsWith("k")) return 9.80665002864 * val;
          else if (unit.startsWith("l")) return 4.44822 * val;
          else return val;
        }
      }

      double parse_speed_constraint (JTextField tf, String prop_name, String dflt) {
        String[] input = tf.getText().trim().split("\\s+");
        switch (input.length) {
        case 0: 
          dflt = (dflt==null || 
                  dflt.equals("") || 
                  !dflt.matches("[+-\\.]?\\d*[\\.]?\\d*")) 
            ? "10" : dflt;
          tf.setText(getParamOrProp(prop_name, dflt));
          return parse_speed_constraint(tf, prop_name, dflt);
        case 1:
          String s = input[0].toLowerCase();
          if (
              separate_suffix(s, tf, "kt")  ||
              separate_suffix(s, tf, "kts") || 
              separate_suffix(s, tf, "km/h") ||
              separate_suffix(s, tf, "m/s") ||
              separate_suffix(s, tf, "mph") ||
              separate_suffix(s, tf, "fps") ||
              // odd ones            
              separate_suffix(s, tf, "f/s") ||
              separate_suffix(s, tf, "kmh") ||
              false)
            return parse_speed_constraint(tf, prop_name, dflt);
          else {
            tf.setText(s + " km/h");
            return parse_speed_constraint(tf, prop_name, dflt);
          }
        default:
          double val = Double.parseDouble(input[0].toLowerCase());
          String unit = input[1].toLowerCase();
          return convert_speeds_to_kmh(val, unit);
        }
      }

      double convert_speeds_to_kmh (double val, String unit) {
        if      (unit.startsWith("km/h")) return val;
        else if (unit.startsWith("kt")) return 1.852 * val;
        else if (unit.startsWith("mph")) return 1.60934 *val;
        else if (unit.startsWith("m/s")) return 3.6 *val;
        else if (unit.startsWith("fps")) return 1.09728 *val;
        else if (unit.startsWith("f/s")) return 1.09728 *val;
        else return val;
      }

      double convert_len_to_m (double val, String unit) {
        if      (unit.startsWith("cm")) return 0.01*val;
        else if (unit.startsWith("ft")) return 0.3048 * val;
        else if (unit.startsWith("in")) return 0.0254 *val;
        else if (unit.startsWith("mm")) return 0.001*val;
        else return val;
      }
    }  // Flight

    class Env extends Panel { // currently now shown to simplify UI
      FoilBoard app;
      LeftPanel leftPanel;
      RightPanel rightPanel;

      Env(FoilBoard target) {

        app = target;
        setLayout(new GridLayout(1,2,5,5));

        leftPanel = new LeftPanel(app);
        rightPanel = new RightPanel(app);

        add(leftPanel);
        add(rightPanel);
      }

      // Env.loadPanel
      @Override
      public void loadPanel () {
        // so far...
      }

      class LeftPanel extends Panel {
        FoilBoard app;
        JTextField f2, o1,o3, o5;
        JLabel l2, la1,la2;
        JLabel lo1,lo3,lo5;

        void add(JTextField tf, ActionListener al) {
          super.add(tf);
          tf.addActionListener(actionHandler);
        }
     
        LeftPanel (FoilBoard target) {
    
          app = target;
          setLayout(new GridLayout(7,2,2,10));

          la1 = new JLabel("Environmental", JLabel.RIGHT);
          la1.setForeground(Color.blue);
          la2 = new JLabel("Settings", JLabel.LEFT);
          la2.setForeground(Color.blue);

          l2 = new JLabel("Altitude ft", JLabel.CENTER);
          f2 = new JTextField("0.0",5);

          lo1 = new JLabel("Press lb/in2", JLabel.CENTER);
          o1 = new JTextField("0.0",5);
          o1.setBackground(color_very_dark);
          o1.setForeground(Color.yellow);

          lo3 = new JLabel("Dens slug/ft3", JLabel.CENTER);
          o3 = new JTextField("0.00027",5);
          o3.setBackground(color_very_dark);
          o3.setForeground(Color.yellow);

          lo5 = new JLabel("Dyn Press lb/ft2", JLabel.CENTER);
          o5 = new JTextField("100.",5);
          o5.setBackground(color_very_dark);
          o5.setForeground(Color.yellow);

          add(la1);
          add(la2);

          add(l2);
          add(f2);

          add(lo1);
          add(o1);

          add(lo3);
          add(o3);

          add(lo5);
          add(o5);

        }

        ActionListener actionHandler = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Double V2,V3;
              double v2,v3;
              float fl1;
              int i1,i2,i3;

              V2 = Double.valueOf(f2.getText());
              v2 = V2.doubleValue();

              alt = v2;
              if (v2 < alt_min) {
                alt = v2 = alt_min;
                fl1 = (float) v2;
                f2.setText(String.valueOf(fl1));
              }
              if (v2 > alt_max) {
                alt = v2 = alt_max;
                fl1 = (float) v2;
                f2.setText(String.valueOf(fl1));
              }
    
              // i2 = (int) (((v2 - alt_min)/(alt_max-alt_min))*1000.);
              // rightPanel.altitude.setValue(i2);

              if (planet == 3) {    // read in the pressure
                double pressure = Double.valueOf(o1.getText()).doubleValue();
                ps0 = pressure /pconv * 2116.;
                if (ps0 < .5) {
                  ps0 = .5;
                  pressure = ps0 / 2116. * pconv;
                  fl1 = (float) pressure;
                  o1.setText(String.valueOf(fl1));
                }
                if (ps0 > 5000.) {
                  ps0 = 5000.;
                  pressure = ps0 / 2116. * pconv;
                  fl1 = (float) pressure;
                  o1.setText(String.valueOf(fl1));
                }
              }

              if (planet == 4) {    // read in the density
                double density = Double.valueOf(o3.getText()).doubleValue();
                rho_EN = density;
                if (lunits == METRIC) rho_EN = density / 515.378819;
                if (rho_EN < .000001) {
                  rho_EN = .000001;
                  density = rho_EN;
                  if (lunits == METRIC) density = rho_EN * 515.4;
                  fl1 = (float) density;
                  o3.setText(String.valueOf(fl1));
                }
                if (rho_EN > 3.0) {
                  rho_EN = 3.;
                  density = rho_EN;
                  if (lunits == METRIC) density = rho_EN * 515.4;
                  fl1 = (float) density;
                  o3.setText(String.valueOf(fl1));
                }
                rho_SI = 515.378819 * rho_EN;              
              }

              //  set limits on spin
              if (foil_is_cylinder_or_ball(current_part.foil)) cylShape.setLims();

              con.recomp_all_parts();
            }}; // actionHandler
      }  // LeftPanel

      class RightPanel extends Panel {
        FoilBoard app;
        JComboBox plntch;
        RightPanel2 rightPanel2;
        RightPanel3 rightPanel3;
        RightPanel6 rightPanel6;

        RightPanel (FoilBoard target) {
          int i1,i2, iAoA;

          app = target;
          setLayout(new GridLayout(7,1,2,10));

          i1 = (int) (((100.0 - v_min)/(v_max-v_min))*1000.);
          i2 = (int) (((0.0 - alt_min)/(alt_max-alt_min))*1000.);
          iAoA = (int) (((0.0 - ang_min)/(ang_max-ang_min))*1000.);

          plntch = new JComboBox();
          plntch.addItem("Earth - Average Day");
          plntch.addItem("Mars - Average Day");
          plntch.addItem("Water-Const Density");
          plntch.addItem("Specify Air T & P");
          plntch.addItem("Specify Density or Viscosity");
          plntch.addItem("Venus - Surface");
          plntch.setBackground(Color.white);
          plntch.setForeground(Color.blue);
          plntch.setSelectedIndex(0);

          rightPanel2 = new RightPanel2(app);
          rightPanel3 = new RightPanel3(app);
          rightPanel6 = new RightPanel6(app);

          add(plntch);
          // System.out.println("-- planet: " + planet);
          plntch.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {

                int i1,i2;
                double v1,v2;
                float fl1,fl2;

                planet  = plntch.getSelectedIndex();
                System.out.println("-- actionPerformed planet: " + planet);
                new Exception().printStackTrace(System.out);

                if (planet == 2) {
                  velocity = 5.;
                  vmax = 40.;
                  if (lunits == 1) vmax = 60.;
                  alt = 0.0;
                  altmax = strut.span;
                  current_part.area = 10.0;
                  // armax = 50.;
                }
                else {
                  vmax = 250.;
                  if (lunits == 1) vmax = 400.;
                  altmax = strut.span;
                  // armax = 2500.;
                }

                if (planet == 0 || planet == 3) {
                  rightPanel.rightPanel6.o6.setBackground(Color.white);
                  rightPanel.rightPanel6.o6.setForeground(color_very_dark);
                }
                if (planet == 1 || planet == 2) {
                  rightPanel.rightPanel6.o6.setBackground(color_very_dark);
                  rightPanel.rightPanel6.o6.setForeground(Color.yellow);
                }
                if (planet == 4 || planet == 6) {
                  rightPanel.rightPanel6.o6.setBackground(color_very_dark);
                  rightPanel.rightPanel6.o6.setForeground(Color.yellow);
                }

                if (planet == 3) {
                  leftPanel.o1.setBackground(Color.white);
                  leftPanel.o1.setForeground(color_very_dark);
                  rightPanel.rightPanel2.o2.setBackground(Color.white);
                  rightPanel.rightPanel2.o2.setForeground(color_very_dark);
                }
                else {
                  leftPanel.o1.setBackground(color_very_dark);
                  leftPanel.o1.setForeground(Color.yellow);
                  rightPanel.rightPanel2.o2.setBackground(color_very_dark);
                  rightPanel.rightPanel2.o2.setForeground(Color.yellow);
                }

                if (planet == 4) {
                  leftPanel.o3.setBackground(Color.white);
                  leftPanel.o3.setForeground(color_very_dark);
                  rightPanel.rightPanel3.o4.setBackground(Color.white);
                  rightPanel.rightPanel3.o4.setForeground(color_very_dark);
                }
                else {
                  leftPanel.o3.setBackground(color_very_dark);
                  leftPanel.o3.setForeground(Color.yellow);
                  rightPanel.rightPanel3.o4.setBackground(color_very_dark);
                  rightPanel.rightPanel3.o4.setForeground(Color.yellow);
                }

                //layplt.show(in.grf.l, foil_is_cylinder_or_ball(current_part.foil) ? "second" : "first");

                //layout.show(out, "first");

                solver.getFreeStream ();
                computeFlowAndRegenPlotAndAdjust();

              } // action
            });

          // System.out.println("-- planet: " + planet);
          add(rightPanel2);
          add(rightPanel3);
          add(rightPanel6);

          add(new JLabel("below is rider drive height slider:"));
          JScrollBar test = new JScrollBar(JScrollBar.HORIZONTAL, 300,10, 0, 1000);
          test.addAdjustmentListener(new AdjustmentListener() {
              public void adjustmentValueChanged(AdjustmentEvent evt) {
                if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                // center_of_lift = adjustmentEvent.getValue()/1000.0;
                // System.out.println("-- center_of_lift: " + center_of_lift);
                RIDER_DRIVE_HEIGHT = 0.5+ evt.getValue()/1000.0;
              }});
          add(test);

        }

        class RightPanel6 extends Panel {
          FoilBoard app;
          JTextField o6;
          JLabel lo6;

          RightPanel6 (FoilBoard target) {
            app = target;
            setLayout(new GridLayout(1,2,2,10));

            lo6 = new JLabel("Rel Humid %", JLabel.LEFT);
            o6 = new JTextField("0.0",5);
            o6.setBackground(Color.white);
            o6.setForeground(color_very_dark);

            add(lo6);
            add(o6);
            o6.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  Double V1;
                  double v1;
                  float fl1;
  
                  V1 = Double.valueOf(o6.getText());
                  v1 = V1.doubleValue();
                  rlhum = v1;
                  if (rlhum < 0) {
                    rlhum = 0;
                    v1 = rlhum;
                    fl1 = (float) v1;
                    o6.setText(String.valueOf(fl1));
                  }
                  if (rlhum > 100.0) {
                    rlhum = 100.;
                    v1 = rlhum;
                    fl1 = (float) v1;
                    o6.setText(String.valueOf(fl1));
                  }

                  solver.getFreeStream ();
                  con.recomp_all_parts();

                } });
          }
        }

        class RightPanel3 extends Panel {
          FoilBoard app;
          JTextField o4;
          JLabel lo4;

          RightPanel3 (FoilBoard target) {
            app = target;
            setLayout(new GridLayout(1,2,2,10));

            lo4 = new JLabel("Visc slug/ft-s", JLabel.LEFT);
            o4 = new JTextField("0.0",5);
            o4.setBackground(color_very_dark);
            o4.setForeground(Color.yellow);

            add(lo4);
            add(o4);
            o4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  Double V1;
                  double v1;
                  float fl1;
  
                  if (planet == 4) {    // read in viscosity
                    V1 = Double.valueOf(o4.getText());
                    v1 = V1.doubleValue();
                    viscos = v1;
                    if (lunits == METRIC) viscos = v1 /47.87;
                    if (viscos < .0000001) {
                      viscos = .0000001;
                      v1 = viscos;
                      if (lunits == METRIC) v1 = viscos * 47.87;
                      fl1 = (float) v1;
                      o4.setText(String.valueOf(fl1));
                    }
                    if (viscos > 3.0) {
                      viscos = 3.;
                      v1 = viscos;
                      if (lunits == METRIC) v1 = viscos * 47.87;
                      fl1 = (float) v1;
                      o4.setText(String.valueOf(fl1));
                    }
                  }

                  con.recomp_all_parts();
                }});
          }
        }

        class RightPanel2 extends Panel {
          FoilBoard app;
          JTextField o2;
          JLabel lo2;

          RightPanel2 (FoilBoard target) {
            app = target;
            setLayout(new GridLayout(1,2,2,10));

            lo2 = new JLabel("Temp-F", JLabel.CENTER);
            o2 = new JTextField("12.5",5);
            o2.setBackground(color_very_dark);
            o2.setForeground(Color.yellow);

            add(lo2);
            add(o2);
            o2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  Double V1;
                  double v1;
                  float fl1;

                  if (planet == 3) {    // read in the temperature
                    V1 = Double.valueOf(o2.getText());
                    v1 = V1.doubleValue();
                    ts0 = v1 + 460.;
                    if (lunits == 1) ts0 = (v1 + 273.1)*9.0/5.0;
                    if (ts0 < 350.) {
                      ts0 = 350.;
                      v1 = ts0 - 460.;
                      if (lunits == 1) v1 = ts0*5.0/9.0 - 273.1;
                      fl1 = (float) v1;
                      o2.setText(String.valueOf(fl1));
                    }
                    if (ts0 > 660.) {
                      ts0 = 660.;
                      v1 = ts0 - 460.;
                      if (lunits == 1) v1 = ts0*5.0/9.0 - 273.1;
                      fl1 = (float) v1;
                      o2.setText(String.valueOf(fl1));
                    }
                  }

                  solver.getFreeStream ();
                  con.recomp_all_parts();
                }});
          }
        }

      }  // RightPanel 
    } // Env

    class Shape extends InputPanel {
      FoilBoard app;
      LeftPanel leftPanel;
      RightPanel rightPanel;

      Button symm_foil_bt, flat_bottom_bt, neg_camb_bt;
      Button hi_camb_bt, flat_plate_bt;
      Button ellipse_bt, curve_plate_bt;
      Button[] old_style_buttons;
      JScrollBar sb_ci_eff;

      Shape (FoilBoard target) {

        app = target;
        setLayout(new GridLayout(1,2,5,5));

        leftPanel = new LeftPanel(app);
        rightPanel = new RightPanel(app);

        add(leftPanel);
        add(rightPanel);

        old_style_buttons = new Button[] {
          symm_foil_bt, flat_bottom_bt, neg_camb_bt,
          hi_camb_bt,flat_plate_bt, ellipse_bt,curve_plate_bt
        };
      }

      // what needs be done in general when user altered current foil shape?
      void recompute () {
        current_part.t_Cl = current_part.t_Cd = null;
        computeFlowAndRegenPlot();
      }

      void set_camber_and_thickness_controls (boolean enable) {
        leftPanel.f_camber.setEnabled(enable);
        leftPanel.f_thickness.setEnabled(enable);
        rightPanel.s1.setEnabled(enable);
        rightPanel.s2.setEnabled(enable);
      }

      boolean on_load;

      // Shape.loadPanel
      @Override
      public void loadPanel () {
        long loadPanel_time = System.currentTimeMillis();
        on_load = true;

        // System.out.println("-- loadPanel current_part: " + current_part);
        current_part.foil.adjust_foil_shape_in_tab();
        
        // these trigger events, do first
        rightPanel.s3.setValue((int) (((current_part.aoa - ang_min)/(ang_max-ang_min))*1000.));
        rightPanel.s1.setValue((int) (((current_part.camber - ca_min)/(ca_max-ca_min))*1000.));
        rightPanel.s2.setValue((int) (((current_part.thickness - thk_min)/(thk_max-thk_min))*1000.));
        sb_ci_eff.setValue((int) (((current_part.Ci_eff - 0)/(3-0))*1000.));

        rightPanel.shape_choice.setSelectedIndex(current_part.foil.id);

        leftPanel.f_angle.setText(pprint(filter3(current_part.aoa)));
        leftPanel.f_camber.setText(pprint(filter3(current_part.camber)));
        leftPanel.f_thickness.setText(pprint(filter1(current_part.thickness)));

        on_load = false;

        //tt 
        // System.out.println("-- Shape loadPanel_time, ms: " + (System.currentTimeMillis()-loadPanel_time)); //System.exit(0);

      }

      class LeftPanel extends Panel {
        FoilBoard app;
        JTextField f_camber, f_thickness, f_angle;
        JLabel l_camber, l_thickness, l_angle;
     
        LeftPanel (FoilBoard target) {
      
          app = target;
          setLayout(new GridLayout(8,2,2,10));

          JLabel l;
          add(l = new JLabel("Foil", JLabel.RIGHT)); l.setForeground(Color.blue);
          add(l = new JLabel("Shape", JLabel.LEFT));  l.setForeground(Color.blue);

          add(new JLabel(""));
          add(new JLabel(""));

          add(l_angle = new JLabel("Angle deg", JLabel.CENTER));
          add(f_angle = new JTextField("5.0",5));
          f_angle.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                double v = Double.valueOf(f_angle.getText()).doubleValue();
                current_part.aoa = v;
                if (v < ang_min) {
                  current_part.aoa = v = ang_min;
                  float fl1 = (float) v;
                  f_angle.setText(String.valueOf(fl1));
                }
                else if (v > ang_max) {
                  current_part.aoa = v = ang_max;
                  float fl1 = (float) v;
                  f_angle.setText(String.valueOf(fl1));
                }
                int i = (int) (((v - ang_min)/(ang_max-ang_min))*1000.);
                rightPanel.s3.setValue(i);
                recompute();
              }});

          add(l_camber = new JLabel("Camber % chord", JLabel.CENTER));
          add(f_camber = new JTextField("0.0",5));
          f_camber.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                double v = Double.valueOf(f_camber.getText()).doubleValue();
                current_part.camber = v;
                if (v < ca_min) {
                  current_part.camber = v = ca_min;
                  float fl1 = (float) v;
                  f_camber.setText(String.valueOf(fl1));
                }
                else if (v > ca_max) {
                  current_part.camber = v = ca_max;
                  float fl1 = (float) v;
                  f_camber.setText(String.valueOf(fl1));
                }
                int i = (int) (((v - ca_min)/(ca_max-ca_min))*1000.);
                rightPanel.s1.setValue(i);
                recompute();
              }});
          add(l_thickness = new JLabel("Thickness %", JLabel.CENTER));
          add(f_thickness = new JTextField("12.5",5));
          f_thickness.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                double v = Double.valueOf(f_thickness.getText()).doubleValue();
                current_part.thickness = v;
                if (v < thk_min) {
                  current_part.thickness = v = thk_min;
                  float fl1 = (float) v;
                  f_thickness.setText(String.valueOf(fl1));
                }
                else if (v > thk_max) {
                  current_part.thickness = v = thk_max;
                  float fl1 = (float) v;
                  f_thickness.setText(String.valueOf(fl1));
                }
                int i = (int) (((v - thk_min)/(thk_max-thk_min))*1000.);
                rightPanel.s2.setValue(i);
                recompute();
              }});

          add(new JLabel("Tip Perform"));
          add(new JLabel("Factor"));

          add(l = new JLabel("Quick Shapes:", JLabel.RIGHT)); l.setForeground(color_very_dark);

          add(symm_foil_bt = new Button ("Symmetric"));
          symm_foil_bt.setBackground(Color.white);
          symm_foil_bt.setForeground(Color.blue);
          symm_foil_bt.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                for (Button b : old_style_buttons) b.setBackground(Color.WHITE);
                symm_foil_bt.setBackground(Color.yellow);
                current_part.camber = 0.0;
                current_part.thickness = 9; // was 12 - too much for hydro 
                buttons_action_epilogue();
              }});

          add(flat_bottom_bt = new_button("Flat Bottom"));
          flat_bottom_bt.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                for (Button b : old_style_buttons) b.setBackground(Color.WHITE);
                flat_bottom_bt.setBackground(Color.yellow);
                current_part.camber = 2.5; // was 5 - too much for hydro 
                current_part.thickness = 9; // was 12 - too much for hydro 
                buttons_action_epilogue();
              }});

          add(neg_camb_bt = new_button("Neg. Camber"));
          neg_camb_bt.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                for (Button b : old_style_buttons) b.setBackground(Color.WHITE);
                neg_camb_bt.setBackground(Color.yellow);
                current_part.camber = -4.0; // was 5 - too much for hydro 
                current_part.thickness = 9; // was 12 - too much for hydro 
                buttons_action_epilogue();
              }});
        }

        void buttons_action_epilogue() {
          setFoil(FOIL_NACA4);

          leftPanel.f_camber.setText(String.valueOf(current_part.camber));
          leftPanel.f_thickness.setText(String.valueOf(current_part.thickness));

          int i1 = (int) (((current_part.camber - ca_min)/(ca_max-ca_min))*1000.);
          int i2 = (int) (((current_part.thickness - thk_min)/(thk_max-thk_min))*1000.);
    
          rightPanel.s1.setValue(i1);
          rightPanel.s2.setValue(i2);

          //ttshch rightPanel.shape_choice.setSelectedIndex(FOIL_NACA4.id);
          if (use_cylinder_shapes)
            in.cylShape.rightPanel.shape_choice.setSelectedIndex(FOIL_JOUKOWSKI.id);
          recompute();
        }
      }  // LeftPanel 


      class RightPanel extends Panel {
        FoilBoard app;
        JScrollBar s1,s2,s3;
        JComboBox shape_choice;
        RightPanel1 rightPanel1;
        RightPanel2 rightPanel2;

        RightPanel (FoilBoard target) {
          int i1,i2,i3;

          app = target;
          setLayout(new GridLayout(8,1,2,10));

          rightPanel1 = new RightPanel1(app);
          rightPanel2 = new RightPanel2(app);

          i1 = (int) (((0.0 - ca_min)/(ca_max-ca_min))*1000.);
          i2 = (int) (((12.5 - thk_min)/(thk_max-thk_min))*1000.);
          i3 = (int) (((current_part.aoa - ang_min)/(ang_max-ang_min))*1000.);

          s1 = new JScrollBar(JScrollBar.HORIZONTAL,i1,10,0,1000);
          s1.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              int pos = s1.getValue();
              current_part.camber = pos * (ca_max - ca_min)/ 1000. + ca_min;
              leftPanel.f_camber.setText(String.valueOf((float)current_part.camber));
              recompute();
            }});

          s2 = new JScrollBar(JScrollBar.HORIZONTAL,i2,10,0,1000);
          s2.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              int pos = s2.getValue();
              current_part.thickness = pos * (thk_max - thk_min)/ 1000. + thk_min;
              leftPanel.f_thickness.setText(String.valueOf((float)current_part.thickness));
              recompute();
            }});

          s3 = new JScrollBar(JScrollBar.HORIZONTAL,i3,10,0,1000);
          s3.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              int pos = s3.getValue();
              current_part.aoa  = pos * (ang_max - ang_min)/ 1000. + ang_min;
              leftPanel.f_angle.setText(String.valueOf((float)current_part.aoa));
              recompute();
            }});

          shape_choice = new JComboBox();
          for (int id = 0; id < foil_arr.length; id++) {
            String text = foil_arr[id].descr;
            shape_choice.addItem(text);
          }
          shape_choice.setBackground(Color.white);
          shape_choice.setForeground(Color.blue);
          // shape_choice.setSelectedIndex(FOIL_JOUKOWSKI.id);
          shape_choice.setSelectedIndex(foil_arr[0].id);

          shape_choice.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                int id = shape_choice.getSelectedIndex();
                String foil = (String)shape_choice.getItemAt(id);
                // System.out.println("-- shape_choice ActionEvent  id: " + id + " foil: " + foil);
                setFoil(foil);
                in.update_state();
              }});

          add(shape_choice);

          Button import_bt;
          add(import_bt = new Button ("Import from File"));
          import_bt.addActionListener(new ActionListener() {
          @Override
          /**
           * @j2sNative
           * 
           * var can_not_do_it_in_js;
           */
          public void actionPerformed(ActionEvent e) {
            Import imp = null;
            try { // Import depends on mhclasses.jar, so have that handled...
              imp = new Import();
            } catch (Throwable err) {
              if (err.toString().indexOf("AirfoilGeometry") > -1)
                System.out.println("Warning: can not import foil profile because file mhclasses.jar can not be found");
              else {
                System.out.println("Warning: can not import, got error:" + err);
                err.printStackTrace(System.out);
              }
              return;
            }
            // adjust name for images
            // maybe we have it?
            Foil foil = current_part.foil;
            if ((foil = (Foil)foils.get(imp.getName())) != null) {
              System.out.println("Warning: file " + imp.file_name + " defines foil " + imp.getName() + "which is already supported, skipping import");
              System.out.println("         the previous defined foil will be used now. If you want to use the shape from file");
              if (imp.from_image)
                System.out.println("         " + imp.file_name + ", then alter the name of the image file");
              else
                System.out.println("         " + imp.file_name + ", then alter the name of foil specified inside the file and reload");
              
            } else {
              imp.analyze();
              if (current_part == strut && imp.camber_pst != 0)
                imp.fixSymmetry("Mast"); // most import data leads to slight assymetric shape
                
              foil = new Tab25Foil(imp.getName(), "Imported foil " + imp.getName(), "\n\n Geometry: \n" + imp.getGeometryAsText() + "\n\n Table: \n" + imp.descr,
                                   imp.thickness_pst, imp.camber_pst, 
                                   imp.Cl, imp.Cd,
                                   null);
              foil.geometry = imp.getGeometry();
              foil.camber_line = imp.getCamberLine();

              int count  = imp.points_count();
              foil.points_x = new double[count];
              foil.points_y = new double[count];
              imp.getPoints(foil.points_x, foil.points_y);
              shape_choice.addItem(foil.descr);
              if (use_cylinder_shapes)
                in.cylShape.rightPanel.shape_choice.addItem(foil.descr);
            }
            in.shp.loadPanel();
          }});

          add(s3);
          add(s1);
          add(s2);
          
          sb_ci_eff = new JScrollBar(JScrollBar.HORIZONTAL,i1,10,0,1000);
          sb_ci_eff.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              if (on_load) return;

              int i1 = evt.getValue();
              current_part.Ci_eff = filter3(i1 * (3-0)/ 1000. + 0);
              System.out.println("-- sb_ci_eff Ci_eff: " + current_part.Ci_eff);
              if (current_part.Ci_eff == 0) 
                new Exception("======== current_part.Ci_eff == 0 ==========" + current_part.name).printStackTrace(System.out);
              con.recomp_all_parts();
            }});
          add(sb_ci_eff);

          add(rightPanel1);
          add(rightPanel2);
        }

        class RightPanel1 extends Panel {
          FoilBoard app;

          RightPanel1 (FoilBoard target) {

            app = target;
            setLayout(new GridLayout(1,2,2,10));

            hi_camb_bt = new_button("High Camber");
            flat_plate_bt = new_button("Flat Plate");

            hi_camb_bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  shape_choice.setSelectedIndex(FOIL_NACA4.id);
                  if (use_cylinder_shapes)
                    in.cylShape.rightPanel.shape_choice.setSelectedIndex(FOIL_NACA4.id);

                  for (Button b : old_style_buttons) b.setBackground(Color.WHITE);
                  hi_camb_bt.setBackground(Color.yellow);

                  current_part.camber = 11.0; // was 15 - too much for hydro 
                  current_part.thickness = 9; // was 12 - to much for hydro

                  if (use_foilsim_foils) {
                    setFoil(FOIL_JOUKOWSKI);
                    shape_choice.setSelectedIndex(FOIL_JOUKOWSKI.id);
                    if (use_cylinder_shapes)
                      in.cylShape.rightPanel.shape_choice.setSelectedIndex(FOIL_JOUKOWSKI.id);
                  }

                  leftPanel.f_camber.setText(String.valueOf(current_part.camber));
                  leftPanel.f_thickness.setText(String.valueOf(current_part.thickness));

                  int i1 = (int) (((current_part.camber - ca_min)/(ca_max-ca_min))*1000.);
                  int i2 = (int) (((current_part.thickness - thk_min)/(thk_max-thk_min))*1000.);
    
                  rightPanel.s1.setValue(i1);
                  rightPanel.s2.setValue(i2);

                  recompute();

                }});

            flat_plate_bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  shape_choice.setSelectedIndex(FOIL_NACA4.id);
                  if (use_cylinder_shapes)
                    in.cylShape.rightPanel.shape_choice.setSelectedIndex(FOIL_NACA4.id);

                  for (Button b : old_style_buttons) b.setBackground(Color.WHITE);
                  flat_plate_bt.setBackground(Color.yellow);

                  Foil f = use_foilsim_foils ? FOIL_FLAT_PLATE : FOIL_NACA4;
                  setFoil(f);
                  current_part.camber = 0.0;
                  current_part.thickness = 1.0;

                  shape_choice.setSelectedIndex(f.id);
                  if (use_cylinder_shapes)
                    in.cylShape.rightPanel.shape_choice.setSelectedIndex(f.id);

                  leftPanel.f_camber.setText(String.valueOf(current_part.camber));
                  leftPanel.f_thickness.setText(String.valueOf(current_part.thickness));

                  int i1 = (int) (((current_part.camber - ca_min)/(ca_max-ca_min))*1000.);
                  int i2 = (int) (((current_part.thickness - thk_min)/(thk_max-thk_min))*1000.);
    
                  rightPanel.s1.setValue(i1);
                  rightPanel.s2.setValue(i2);

                  recompute();

                }});

            add(hi_camb_bt);
            add(flat_plate_bt);
          }

          public boolean action (Event evt, Object arg) {
            System.out.println("-- warning: obsolete action() invoked... arg: " + arg);
              return false;
          }

        }  // RightPanel1

        class RightPanel2 extends Panel {
          FoilBoard app;

          RightPanel2 (FoilBoard target) {

            app = target;
            setLayout(new GridLayout(1,2,2,10));

            ellipse_bt = new_button("Ellipse");
            ellipse_bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  for (Button b : old_style_buttons) b.setBackground(Color.WHITE);
                  ellipse_bt.setBackground(Color.yellow);
                  setFoil(FOIL_ELLIPTICAL);
                  current_part.camber = 0.0;
                  current_part.thickness = 12.5;
                  shape_choice.setSelectedIndex(FOIL_ELLIPTICAL.id);
                  in.cylShape.rightPanel.shape_choice.setSelectedIndex(FOIL_ELLIPTICAL.id);
                  leftPanel.f_camber.setText(String.valueOf(current_part.camber));
                  leftPanel.f_thickness.setText(String.valueOf(current_part.thickness));

                  int i1 = (int) (((current_part.camber - ca_min)/(ca_max-ca_min))*1000.);
                  int i2 = (int) (((current_part.thickness - thk_min)/(thk_max-thk_min))*1000.);
    
                  rightPanel.s1.setValue(i1);
                  rightPanel.s2.setValue(i2);
                  recompute();
                }});

            curve_plate_bt = new_button("Curve Plate");
            curve_plate_bt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  for (Button b : old_style_buttons) b.setBackground(Color.WHITE);
                  curve_plate_bt.setBackground(Color.yellow);
                  Foil f = use_foilsim_foils ? FOIL_FLAT_PLATE : FOIL_NACA4;
                  setFoil(f);
                  //current_part.aoa = 5.0;
                  current_part.camber = 5.0;
                  current_part.thickness = 1.0;

                  shape_choice.setSelectedIndex(f.id);
                  if (use_cylinder_shapes)
                    in.cylShape.rightPanel.shape_choice.setSelectedIndex(f.id);

                  int i1 = (int) (((current_part.camber - ca_min)/(ca_max-ca_min))*1000.);
                  int i2 = (int) (((current_part.thickness - thk_min)/(thk_max-thk_min))*1000.);
    
                  rightPanel.s1.setValue(i1);
                  rightPanel.s2.setValue(i2);
                  recompute();
                }});

            if (use_foilsim_foils)
              add(ellipse_bt);
            add(curve_plate_bt);
          }

          public boolean action (Event evt, Object arg) {
            System.out.println("-- warning: obsolete action() invoked... arg: " + arg);
            return false;
          }

        }  // RightPanel2
      }  // RightPanel
    }  // Shape 

    class Size extends InputPanel {
      FoilBoard app;
      LeftPanel leftPanel;
      RightPanel rightPanel;

      double xpos_min = -1, xpos_max = 1; // in meters

      Size (FoilBoard target) {

        app = target;
        setLayout(new GridLayout(1,2,5,5));

        leftPanel = new LeftPanel(app);
        rightPanel = new RightPanel(app);

        add(leftPanel);
        add(rightPanel);
      }

      JLabel addJLabel (String text, Color fg, int align) {
        JLabel lb = new JLabel(text, align);
        if (fg != null) lb.setForeground(fg);
        add(lb);
        return lb;
      }

      int set_to_units = IMPERIAL;

      // Size.loadPanel 
      @Override // FoilBoard.Panel
      public void loadPanel () {
        if (DEBUG_SPEED_SUPPR) return;
        // System.out.println("-- Size.loadPanel enter");
        if (on_loadPanel) return;
        // System.out.println("-- Size.loadPanel continue - not on_loadPanel");
        try {
          on_loadPanel = true;
          leftPanel.part_name.setText(current_part.name);
          if (true) { // because of MAC/Chord can not use (set_to_units != display_units) here
            set_to_units = display_units;
            String chord_prefix  = current_part.chord_spec.length == 1 ? "Chord" : "MAC";
            switch (display_units) {
            case IMPERIAL: 
            case NAVAL: 
              leftPanel.size_lbl.setText(chord_prefix + " in");
              leftPanel.span_lbl.setText("Span in");
              leftPanel.xpos_lbl.setText("Xpos in");
              leftPanel.area_lbl.setText("Area sq in");
              break;
            case METRIC:
              leftPanel.size_lbl.setText(chord_prefix + " m");
              leftPanel.span_lbl.setText("Span m");
              leftPanel.xpos_lbl.setText("Xpos m");
              leftPanel.area_lbl.setText("Area sq m");
              break;
            case METRIC_2:
              leftPanel.size_lbl.setText(chord_prefix + " cm");
              leftPanel.span_lbl.setText("Span cm");
              leftPanel.xpos_lbl.setText("Xpos cm");
              leftPanel.area_lbl.setText("Area-sq cm");
              break;
            case IMPERIAL_FEET: 
              leftPanel.size_lbl.setText(chord_prefix + " ft");
              leftPanel.span_lbl.setText("Span ft");
              leftPanel.xpos_lbl.setText("Xpos ft");
              leftPanel.area_lbl.setText("Area sq ft");
              break;
            }
          }

          // assume xpos of fuse shoudl be always 0
          {
            leftPanel.xpos_tf.setEnabled(current_part != fuse);
            rightPanel.xpos_SB.setEnabled(current_part != fuse);
          }

          // these cause events, so do them first.
          int pos = (int) (((current_part.chord - chrd_min)/(chrd_max-chrd_min))*1000.);
          rightPanel.chord_SB.setValue(pos);
          rightPanel.span_SB.setValue((int) (((current_part.span - span_min)/(span_max-span_min))*1000.));
          rightPanel.xpos_SB.setValue((int) (((current_part.xpos - xpos_min)/(xpos_max-xpos_min))*1000.));
          if (false) // temporarily disabing area_SB because if cross-editing event collisions..
            rightPanel.area_SB.setValue((int) (((current_part.area - ar_min)/(ar_max-ar_min))*1000.));

          leftPanel.size_tf.setText(make_size_info_in_display_units(current_part.chord, false));
          leftPanel.span_tf.setText(make_size_info_in_display_units(current_part.span, false));
          leftPanel.xpos_tf.setText(make_size_info_in_display_units(current_part.xpos, false));
          leftPanel.area_tf.setText(make_area_info_in_display_units(current_part.area, false));
          
          leftPanel.aspect_tf.setText(""+filter1or3(current_part.aspect_rat));
        } catch (Throwable t) { on_loadPanel = false; throw t;} 
        on_loadPanel = false;
      }

      class LeftPanel extends Panel {
        FoilBoard app;
        JTextField size_tf, span_tf, xpos_tf, area_tf, aspect_tf;
        JLabel part_name, size_lbl, span_lbl, xpos_lbl, area_lbl;
    
        LeftPanel (FoilBoard target) {
   
          app = target;
          setLayout(new GridLayout(6,2,2,10));

          part_name = addJLabel("---", Color.blue, JLabel.RIGHT);

          size_lbl = new JLabel("Chord-ft", JLabel.CENTER);

          size_tf = new JTextField("5.0",5);
          size_tf.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                Double V1 = Double.valueOf(size_tf.getText());
                //debug System.out.println("-chord - ActionEvent: " + e);
                double chord = V1.doubleValue();
                chord = limit(chrd_min,
                              size_input_in_display_units_to_m(chord, display_units),
                              chrd_max);
                if (current_part.chord == chord) return;
                current_part.chord = chord;
                if (current_part.chord_spec.length > 1) { // part loses its multisegemnted shape!
                  // prb, need alert here.. 
                  System.out.println("WARNING: part " + current_part.name + " loses its multisegemnted shape " +
                                     "now chord=" + chord);                  
                }
                current_part.chord_spec = new String[] {""+chord};
                
                parseParamData(current_part, current_part.name, current_part.toDefString());

                current_part.area = current_part.span * chord;

                current_part.aspect_rat = current_part.span/chord;
                current_part.spanfac = (int)(2.0*fact*current_part.aspect_rat*.3535);

                // no need - loadPanel does it...
                // sync up slider, this fires up event
                // rightPanel.chord_SB.setValue((int) (((current_part.chord - chrd_min)/(chrd_max-chrd_min))*1000.));
                
                computeFlowAndRegenPlot();
                size.loadPanel();
              }});

          span_lbl = new JLabel("Span-ft", JLabel.CENTER);
          span_tf = new JTextField("20.0",5);
          span_tf.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                Double V2 = Double.valueOf(span_tf.getText());
                double span = V2.doubleValue();
                span = limit(span_min, 
                             size_input_in_display_units_to_m(span, display_units),
                             span_max);
                if (current_part.span == span) return;
                current_part.span = span;
                parseParamData(current_part, current_part.name, current_part.toDefString());

                current_part.area = span * current_part.chord;
                current_part.aspect_rat = span*span/current_part.area;

                current_part.spanfac = (int)(2.0*fact*current_part.aspect_rat*.3535);
                computeFlowAndRegenPlot();
                size.loadPanel();
              }});

          xpos_lbl = new JLabel("Xpos-ft", JLabel.CENTER);
          xpos_tf = new JTextField("20.0",5);
          xpos_tf.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                Double V2 = Double.valueOf(xpos_tf.getText());
                double xpos = V2.doubleValue();
                xpos = limit(xpos_min, 
                             size_input_in_display_units_to_m(xpos, display_units),
                             xpos_max);
                
                if (current_part.xpos == xpos) return;
                current_part.xpos = xpos;
                parseParamData(current_part, current_part.name, current_part.toDefString());

                current_part.area = xpos * current_part.chord;

                computeFlowAndRegenPlot();
                size.loadPanel();
              }});


          area_lbl = new JLabel("Area-sq ft", JLabel.CENTER);
          area_tf = new JTextField("100.0",5);
          area_tf.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                Double V3 = Double.valueOf(area_tf.getText());
                double area = V3.doubleValue();
                area = size_input_in_display_units_to_m(area, display_units);
                area = limit(ar_min, area, ar_max);
                double scale_k = area/current_part.area;
                current_part.area = area;
                // scale area preserving aspect ratio.
                current_part.span *= Math.sqrt(scale_k);
                current_part.chord *= Math.sqrt(scale_k);

                computeFlowAndRegenPlot();
                size.loadPanel();
              }});

          aspect_tf = new JTextField("0.0",5); // todo? make not editable?
          aspect_tf.setBackground(color_very_dark);
          aspect_tf.setForeground(Color.yellow);

          add(part_name);
          add(addJLabel("Size", Color.blue, JLabel.LEFT));

          add(size_lbl);
          add(size_tf);

          add(span_lbl);
          add(span_tf);

          add(xpos_lbl);
          add(xpos_tf);

          add(area_lbl);
          add(area_tf);

          add(new JLabel("Aspect Rat", JLabel.CENTER));
          add(aspect_tf);
        }

      }  // LeftPanel 

      class RightPanel extends Panel {
        FoilBoard app;
        ChordSB chord_SB;
        SpanSB span_SB;
        XPosSB xpos_SB;
        AreaSB area_SB;

        RightPanel (FoilBoard target) {
          app = target;
          setLayout(new GridLayout(6,1,2,10));

          chord_SB = new ChordSB(app);
          span_SB = new SpanSB(app);
          xpos_SB = new XPosSB(app);
          area_SB = new AreaSB(app);

          add(new JLabel(" ", JLabel.CENTER));
          add(chord_SB);
          add(span_SB);
          add(xpos_SB);
          // temporarily disabing area_SB because if cross-editing event collisions..
          if (false)
            add(area_SB); 
          else
            add(new JLabel(" ", JLabel.CENTER));

          add(new JLabel(" ", JLabel.CENTER));
        }

        class ChordSB extends JScrollBar {  // chord slider
          FoilBoard app;

          @Override
          public void setValue(int i) {
            if (i == getValue()) return;
            super.setValue(i);
          }

          ChordSB (FoilBoard target) {
            super(JScrollBar.HORIZONTAL,
                  (int)(((current_part.chord - chrd_min)/(chrd_max-chrd_min))*1000.), 
                  10,0,1000);
            app = target;
            addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent evt) {
                  if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                  if (app.in.size.on_loadPanel) return;
                  int i = getValue();
                  //tt System.out.println("-- Chord AdjustmentEvent: i=" + i);
                  double chord  = i * (chrd_max - chrd_min)/ 1000. + chrd_min;
                  if (current_part.chord == chord) return;
                  //debug new Exception("warn!!!! " + current_part.chord +"!=" +  chord).printStackTrace(System.out);
                  
                  current_part.chord = chord;
                  // the following logc is similat to wgat is in chord_tf listener
                  if (current_part.chord_spec.length > 1) { // part loses its multisegemnted shape!
                    // prb, need alert here.. 
                    System.out.println("WARNING: part " + current_part.name + " loses its multisegemnted shape " +
                                       "now chord=" + chord);                  
                  }
                  current_part.chord_spec = new String[] {""+chord};
                  parseParamData(current_part, current_part.name, current_part.toDefString());

                  // rounded up text box value
                  //// leftPanel.size_tf.setText(make_size_info_in_display_units(current_part.chord, false));
                  
                  // recalc area, move slider, update text
                  current_part.area = current_part.span * current_part.chord;
                  //// rightPanel.area_SB.setValue((int) (((current_part.area - ar_min)/(ar_max-ar_min))*1000.));
                  
                  current_part.aspect_rat = current_part.span*current_part.span/current_part.area;
                  current_part.spanfac = (int)(2.0*fact*current_part.aspect_rat*.3535);

                  // old order
                  computeFlowAndRegenPlot();
                  size.loadPanel();
                  
                }});
          }

          public boolean handleEvent (Event evt) {
              System.out.println("-- handleEvent: obsolete");
              return false;
          }
        }  // ChordSB

        class SpanSB extends JScrollBar {  // span slider
          FoilBoard app;

          @Override
          public void setValue(int i) {
            if (i == getValue()) return;
            super.setValue(i);
          }
 
          SpanSB (FoilBoard target) {
            super(JScrollBar.HORIZONTAL,
                  (int) (((current_part.span - span_min)/(span_max-span_min))*1000.),
                  10,0,1000);
            app = target;

            addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent evt) {
                  if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                  if (app.in.size.on_loadPanel) return;
                  int i = getValue();
                  double span  = i * (span_max - span_min)/ 1000. + span_min;
                  if (current_part.span == span) return;
                  current_part.span = span;
                  parseParamData(current_part, current_part.name, current_part.toDefString());

                  current_part.area = current_part.span * current_part.chord;

                  current_part.aspect_rat = current_part.span*current_part.span/current_part.area;
                  current_part.spanfac = (int)(2.0*fact*current_part.aspect_rat*.3535);

                  // old order
                  computeFlowAndRegenPlot();
                  size.loadPanel();
                }});

          }

          public boolean handleEvent (Event evt) {
              System.out.println("-- handleEvent: obsolete");
              return false;
          }
        }  // SpanSB

        class XPosSB extends JScrollBar {  // part's xpos slider
          FoilBoard app;

          @Override
          public void setValue(int i) {
            if (i == getValue()) return;
            super.setValue(i);
          }
 
          XPosSB (FoilBoard target) {
            super(JScrollBar.HORIZONTAL,
                  (int) (((current_part.xpos - xpos_min)/(xpos_max-xpos_min))*1000.),
                  10,0,1000);
            app = target;

            addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent evt) {
                  if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                  if (app.in.size.on_loadPanel) return;
                  int i = getValue();
                  double xpos  = i * (xpos_max - xpos_min)/ 1000. + xpos_min;
                  if (current_part.xpos == xpos) return;
                  current_part.xpos = xpos;
                  parseParamData(current_part, current_part.name, current_part.toDefString());

                  computeFlowAndRegenPlot();
                  size.loadPanel();
                }});

          }

          public boolean handleEvent (Event evt) {
              System.out.println("-- handleEvent: obsolete");
              return false;
          }
        }  // XPosSB

        class AreaSB extends JScrollBar {  // area slider
          FoilBoard app;

          @Override
          public void setValue(int i) {
            if (i == getValue()) return;
            super.setValue(i);
          }
 
          AreaSB (FoilBoard target) {
            super(JScrollBar.HORIZONTAL,
                  (int) (((current_part.area - ar_min)/(ar_max-ar_min))*1000.),
                  10,0,1000);
            app = target;
            addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent evt) {
                  if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                  if (app.in.size.on_loadPanel) return;
                  int i = getValue();
                  double new_area = i * (ar_max - ar_min)/ 1000. + ar_min; 
                  double scale_k = new_area/current_part.area;
                  current_part.area = new_area;

                  // scale sizes preserving aspect
                  // this moves all 3 sliders (and resets all 3 boxes)

                  current_part.chord  *= Math.sqrt(scale_k);
                  //// rightPanel.chord_SB.setValue((int) (((current_part.chord - chrd_min)/(chrd_max-chrd_min))*1000.));

                  current_part.span  *= Math.sqrt(scale_k);
                  //// rightPanel.span_SB.setValue((int) (((current_part.span - span_min)/(span_max-span_min))*1000.));

                  current_part.area = current_part.span * current_part.chord;
                  //// leftPanel.area_tf.setText(make_area_info_in_display_units(current_part.area, false));

                  // old order
                  computeFlowAndRegenPlot();
                  size.loadPanel();
                }});
          }

          public boolean handleEvent (Event evt) {
              System.out.println("-- handleEvent: obsolete");
              return false;
          }
        }  // AreaSB
      }     // RightPanel
    }  // Size 

    class BallCylinderShape extends InputPanel {
      FoilBoard app;
      LeftPanel leftPanel;
      RightPanel rightPanel;

      BallCylinderShape (FoilBoard target) {

        app = target;
        setLayout(new GridLayout(1,2,5,5));

        leftPanel = new LeftPanel(app);
        rightPanel = new RightPanel(app);

        add(leftPanel);
        add(rightPanel);
      }

      @Override
      public void loadPanel () {}

      public void setLims() {
        Double V1;
        double v1;
        float fl1;
        int i1;

        spin_max = 2.75 * velocity/vconv /(radius/lconv);
        spin_min = -2.75 * velocity/vconv/(radius/lconv);
        if (spin*60.0 < spin_min) {
          spin = spin_min/60.0;
          fl1 = (float) (spin*60.0);
          leftPanel.f1.setText(String.valueOf(fl1));
        }
        if (spin*60.0 > spin_max) {
          spin = spin_max/60.0;
          fl1 = (float) (spin*60.0);
          leftPanel.f1.setText(String.valueOf(fl1));
        }
        i1 = (int) (((60*spin - spin_min)/(spin_max-spin_min))*1000.);
        rightPanel.s1.setValue(i1);
      }

      class LeftPanel extends Panel {
        FoilBoard app;
        JTextField f1,f2,f3;
        JLabel l1,l2,l3;
     
        LeftPanel (FoilBoard target) {
     
          app = target;
          setLayout(new GridLayout(6,2,2,10));

          JLabel l = new JLabel("Cylinder-", JLabel.RIGHT); add(l);
          l.setForeground(Color.blue);
          l = new JLabel("Ball Input", JLabel.LEFT); add(l);
          l.setForeground(Color.blue);

          l1 = new JLabel("Spin rpm", JLabel.CENTER);
          f1 = new JTextField("0.0",5);

          l2 = new JLabel("Radius ft", JLabel.CENTER);
          f2 = new JTextField(".5",5);

          l3 = new JLabel("Span ft", JLabel.CENTER);
          f3 = new JTextField("5.0",5);

          f1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              _handleEvent(e); }});
          f2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              _handleEvent(e); }});
          f3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              _handleEvent(e); }});

          add(l1);
          add(f1);

          add(l2);
          add(f2);

          add(l3);
          add(f3);

          add(new JLabel(" ", JLabel.CENTER));
          add(new JLabel(" ", JLabel.CENTER));

          add(new JLabel(" ", JLabel.CENTER));
          add(new JLabel(" ", JLabel.CENTER));
        }

        private void _handleEvent (ActionEvent evt) {
          Double V1,V2,V3;
          double v1,v2,v3;
          float fl1;
          int i1,i2,i3;

          {
            V1 = Double.valueOf(f1.getText());
            v1 = V1.doubleValue();
            V2 = Double.valueOf(f2.getText());
            v2 = V2.doubleValue();
            V3 = Double.valueOf(f3.getText());
            v3 = V3.doubleValue();

            spin = v1;
            if (v1 < spin_min) {
              spin = v1 = spin_min;
              fl1 = (float) v1;
              f1.setText(String.valueOf(fl1));
            }
            if (v1 > spin_max) {
              spin = v1 = spin_max;
              fl1 = (float) v1;
              f1.setText(String.valueOf(fl1));
            }
            spin = spin/60.0;

            radius = v2;
            if (v2 < rad_min) {
              radius = v2 = rad_min;
              fl1 = (float) v2;
              f2.setText(String.valueOf(fl1));
            }
            if (v2 > rad_max) {
              radius = v2 = rad_max;
              fl1 = (float) v2;
              f2.setText(String.valueOf(fl1));
            }
            cylShape.setLims();
   
            current_part.span = v3;
            if (current_part.foil == FOIL_BALL) {
              current_part.span = v3 = radius;
              fl1 = (float) v3;
              f3.setText(String.valueOf(fl1));
            }
            if (v3 < span_min) {
              current_part.span = v3 = span_min;
              fl1 = (float) v3;
              f3.setText(String.valueOf(fl1));
            }
            if (v3 > span_max) {
              current_part.span = v3 = span_max;
              fl1 = (float) v3;
              f3.setText(String.valueOf(fl1));
            }
            current_part.spanfac = (int)(fact*current_part.span/radius*.3535);
            current_part.area = 2.0*radius*current_part.span;
            if (current_part.foil == FOIL_BALL) current_part.area = 3.1415926 * radius * radius;

            i1 = (int) (((v1 - spin_min)/(spin_max-spin_min))*1000.);
            i2 = (int) (((v2 - rad_min)/(rad_max-rad_min))*1000.);
            i3 = (int) (((v3 - span_min)/(span_max-span_min))*1000.);
   
            rightPanel.s1.setValue(i1);
            rightPanel.s2.setValue(i2);
            rightPanel.s3.setValue(i3);

            computeFlowAndRegenPlot();
          }
        } // Handler
      }  // LeftPanel 

      class RightPanel extends Panel {
        FoilBoard app;
        JScrollBar s1,s2,s3;
        JComboBox shape_choice;

        RightPanel (FoilBoard target) {
          int i1,i2,i3;

          app = target;
          setLayout(new GridLayout(6,1,2,10));

          i1 = (int) (((spin*60.0 - spin_min)/(spin_max-spin_min))*1000.);
          i2 = (int) (((radius - rad_min)/(rad_max-rad_min))*1000.);
          i3 = (int) (((current_part.span - span_min)/(span_max-span_min))*1000.);

          s1 = new JScrollBar(JScrollBar.HORIZONTAL,i1,10,0,1000);
          s2 = new JScrollBar(JScrollBar.HORIZONTAL,i2,10,0,1000);
          s3 = new JScrollBar(JScrollBar.HORIZONTAL,i3,10,0,1000);

          s1.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              handleBar(evt);}});
          s2.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              handleBar(evt);}});
          s3.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
              if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
              handleBar(evt);}});

          shape_choice = new JComboBox();

          for (int id = 0; id < foil_arr.length; id++) {
            String text = foil_arr[id].descr;
            shape_choice.addItem(text);
          }
          
          shape_choice.setBackground(Color.white);
          shape_choice.setForeground(Color.blue);
          // shape_choice.setSelectedIndex(FOIL_JOUKOWSKI.id);
          shape_choice.setSelectedIndex(foil_arr[0].id);

          shape_choice.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                setFoil((String)shape_choice.getItemAt(shape_choice.getSelectedIndex()));
                if (in != null) in.update_state();
              }});

          add(shape_choice);
          add(s1);
          add(s2);
          add(s3);
          add(new JLabel(" ", JLabel.CENTER));
          add(new JLabel(" ", JLabel.CENTER));
        }

        private void handleBar (AdjustmentEvent evt) {
          int i1,i2,i3;
          double v1,v2,v3;
          float fl1,fl2,fl3;
              
          // Input for computations
          i1 = s1.getValue();
          i2 = s2.getValue();
          i3 = s3.getValue();

          spin = v1 = i1 * (spin_max - spin_min)/ 1000. + spin_min;
          spin = spin / 60.0;
          radius = v2 = i2 * (rad_max - rad_min)/ 1000. + rad_min;
          current_part.span = v3 = i3 * (span_max - span_min)/ 1000. + span_min;
          if (current_part.foil == FOIL_BALL) current_part.span = v3 = radius;
          current_part.spanfac = (int)(fact*current_part.span/radius*.3535);
          current_part.area = 2.0*radius*current_part.span;
          if (current_part.foil == FOIL_BALL) current_part.area = 3.1415926 * radius * radius;
          cylShape.setLims();

          fl1 = (float) v1;
          fl2 = (float) v2;
          fl3 = (float) v3;

          leftPanel.f1.setText(String.valueOf(fl1));
          leftPanel.f2.setText(String.valueOf(fl2));
          leftPanel.f3.setText(String.valueOf(fl3));
      
          computeFlowAndRegenPlot();
        }
      }  // RightPanel
    }  // BallCylinderShape 

    class PlotSelectorTab extends Panel {
      FoilBoard app;

      ButtonGroup cbg = new ButtonGroup();

      JRadioButton add (String caption, final int type_id) { return add(caption, type_id, 0); }
      JRadioButton add (String caption, final int type_id, final int y_id) {
        JRadioButton cb = new JRadioButton(caption, plot_type == type_id);
        add(cb); 
        cbg.add(cb);
        if (true) // actions - seems better because pressing selected one works
          cb.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                plot_type = type_id;
                plot_y_val = y_id;
                out.setSelectedIndex(0);
                out.plot.loadPlot();
              }});
        else // item changed
          cb.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) {             
            //System.out.println("-- cb itemStateChanged e: " + e);
            plot_type = type_id;
            plot_y_val = y_id;
            out.setSelectedIndex(0);
            out.plot.loadPlot();
          }});
        return cb;
      }

      PlotSelectorTab (FoilBoard target) {
        app = target;

        setLayout(new GridLayout(12,1));
        
        add(new JLabel("Whole Craft + Rider Plots"));
        
        add("Rider C,G, Board Pitch, Total Drag vs Travel Speed", PLOT_TYPE_CG_VS_SPEED);
        add("Drag of Wing,Stab,Mast,Fuse,etc vs Travel Speed", PLOT_TYPE_DRAG_TOTALS_VS_SPEED);

        add(new JLabel("Plots for the Selectet Part (Wing/Stab/Mast/Fuse)"));

        add("Cl/Cd Polar Diagaram", PLOT_TYPE_LIFT_DRAG_POLARS);
        add("Drag Components vs Travel Speed", PLOT_TYPE_CURR_PART_VS_SPEED);
        add("Cl vs Angle", PLOT_TYPE_ANGLE, PLOT_OUT_CL);
        add("Cd vs Angle", PLOT_TYPE_ANGLE, PLOT_OUT_CD);
        add("Lift vs Angle", PLOT_TYPE_ANGLE, PLOT_OUT_LIFT);
        add("Drag vs Angle", PLOT_TYPE_ANGLE, PLOT_OUT_DRAG);
        // not supported yet! add("Cm vs Angle", PLOT_TYPE_ANGLE, PLOT_OUT_CM);
        add("Fluid Pressure Variation alone Chord", PLOT_TYPE_PRESSURE);
        add("Fluid Velocity Variation alone Chord", PLOT_TYPE_VELOCITY);
 
      }

      @Override
      public void loadPanel () { 
        out.plot.loadPlot();
      }

      // old stuff
      //
      // class Upper extends Panel {
      //   FoilBoard app;
      //   JLabel l1;
      //   Button pl1,pl2,pl3, bt_balance_pos_vs_speed, bt_drag_elts_vs_speed;
      // 
      //   Upper(FoilBoard target) {
      //     app = target;
      //     setLayout(new GridLayout(3,0,5,5));
      // 
      //     pl1 = new_button("Pressure");
      // 
      //     pl2 = new_button("Velocity");
      // 
      //     pl3 = new Button ("Drag Polar");
      //     pl3.setBackground(Color.yellow);
      //     pl3.setForeground(Color.blue);
      // 
      //     bt_balance_pos_vs_speed = new_button("CG/Speed");
      //     bt_drag_elts_vs_speed =   new_button("Drag/Speed");
      // 
      //     add(new JLabel(" ", JLabel.RIGHT));
      //     add(new JLabel("Select", JLabel.RIGHT));
      //     add(new JLabel("Plot", JLabel.LEFT));
      //     add(new JLabel(" ", JLabel.RIGHT));
      // 
      //     add(pl1);
      //     add(pl2);
      //     add(pl3);
      //     add(bt_balance_pos_vs_speed);
      // 
      //     add(bt_drag_elts_vs_speed);
      //     add(new JLabel(" ", JLabel.RIGHT));
      //     add(new JLabel(" ", JLabel.RIGHT));
      //     add(new JLabel(" ", JLabel.RIGHT));
      // 
      //   }
      // 
      //   public boolean action (Event evt, Object arg) {
      //     if (evt.target instanceof Button) {
      //       handleBut(evt,arg);
      //       return true;
      //     }
      //     else return false;
      //   } // Handler
      // 
      //   public void handleBut(Event evt, Object arg) {
      //     String label = (String)arg;
      //     out.setSelectedIndex(0);
      // 
      //     ensure_all_array();
      // 
      //     for (Button b : all) { 
      //       b.setBackground(Color.white);
      //       // b.setForeground(Color.blue);
      //     }
      // 
      //     if (label.equals("Pressure")) {
      //       plot_type = PLOT_TYPE_PRESSURE;
      //       pl1.setBackground(Color.yellow);
      //       pl3.setBackground(Color.white);
      //     } else if (label.equals("Velocity")) {
      //       plot_type = PLOT_TYPE_VELOCITY;
      //       pl2.setBackground(Color.yellow);
      //     } else if (label.equals("Drag Polar")) {
      //       plot_type = PLOT_TYPE_LIFT_DRAG_POLARS;
      //       pl3.setBackground(Color.yellow);
      //     } else if (label.equals("CG/Speed")) {
      //       plot_type = PLOT_TYPE_CG_VS_SPEED;
      //       bt_balance_pos_vs_speed.setBackground(Color.yellow);
      //     } else if (label.equals("Drag/Speed")) {
      //       plot_type = PLOT_TYPE_DRAG_TOTALS_VS_SPEED;
      //       bt_drag_elts_vs_speed.setBackground(Color.yellow);
      //     } 
      // 
      //     out.plot.loadPlot();
      // 
      //   } // end handlebut
      // } // Upper
      // 
      // class Lower extends Panel {
      //   FoilBoard app;
      //   FoilCtrls f;
      //   BallCtrls c;
      // 
      //   Lower(FoilBoard target) {
      //     app = target;
      //     layplt = new CardLayout();
      //     setLayout(layplt);
      // 
      //     f = new FoilCtrls(app);
      //     c = new BallCtrls(app);
      // 
      //     add ("first", f);
      //     add ("second", c);
      //   }
      // 
      //   class FoilCtrls extends Panel {
      //     FoilBoard app;
      //     JLabel l2;
      //     Button pl3,pl4,pl5,pl6,pl7,pl8,pl9;
      //     JComboBox plout,ploutb;
      // 
      //     FoilCtrls(FoilBoard target) {
      //       app = target;
      //       setLayout(new GridLayout(3,4,5,5));
      // 
      //       ploutb = new JComboBox();
      //       ploutb.addItem("Lift vs.");
      //       ploutb.addItem("Drag vs.");
      //       ploutb.setBackground(Color.white);
      //       ploutb.setForeground(Color.red);
      //       ploutb.setSelectedIndex(0);
      // 
      //       plout = new JComboBox();
      //       plout.addItem("Lift vs.");
      //       plout.addItem("Cl vs.");
      //       plout.addItem("Drag vs.");
      //       plout.addItem("Cd vs.");
      //       plout.setBackground(Color.white);
      //       plout.setForeground(Color.red);
      //       plout.setSelectedIndex(0);
      // 
      //       pl3 = new_button("Angle");
      //       pl4 = new_button("Thickness");
      //       pl5 = new_button("Camber");
      //       pl6 = new_button("Speed");
      //       pl7 = new_button("Altitude");
      //       pl8 = new_button("Wing Area");
      //       pl9 = new_button("Density");
      // 
      //       add(plout);
      //       add(pl3);
      //       add(pl5);
      //       add(pl4);
      // 
      //       add(ploutb);
      //       add(pl6);
      //       add(pl7);
      //       add(new JLabel(" ", JLabel.RIGHT));
      // 
      //       add(new JLabel(" ", JLabel.RIGHT));
      //       add(pl8);
      //       add(pl9);
      //       add(new JLabel(" ", JLabel.RIGHT));
      //     }
      // 
      //     public boolean action (Event evt, Object arg) {
      //       if (evt.target instanceof Button) {
      //         handleBut(evt,arg);
      //         return true;
      //       }
      //       if (evt.target instanceof JComboBox) {
      //         String label = (String)arg;
      //         plot_y_val = plout.getSelectedIndex();
      //         plot_y_val_2 = ploutb.getSelectedIndex();
      //               
      //         out.plot.loadPlot();
      // 
      //         return true;
      //       }
      //       else return false;
      //     } // Handler
      // 
      //     public void handleBut(Event evt, Object arg) {
      //       String label = (String)arg;
      //       out.setSelectedIndex(0);
      // 
      //       ensure_all_array();
      //       for (Button b : all) { 
      //         b.setBackground(Color.white);
      //       }
      //       if (label.equals("Angle")) {
      //         plot_type = PLOT_TYPE_ANGLE;
      //         pl3.setBackground(Color.yellow);
      //       }
      //       if (label.equals("Thickness")) {
      //         plot_type = PLOT_TYPE_THICKNESS;
      //         pl4.setBackground(Color.yellow);
      //       }
      //       if (label.equals("Camber")) {
      //         plot_type = PLOT_TYPE_CAMBER;
      //         pl5.setBackground(Color.yellow);
      //       }
      //       if (label.equals("Speed")) {
      //         plot_type = PLOT_TYPE_CURR_PART_VS_SPEED;
      //         pl6.setBackground(Color.yellow);
      //       }
      //       if (label.equals("Altitude")) {
      //         plot_type = PLOT_TYPE_ALTITUDE;
      //         pl7.setBackground(Color.yellow);
      //       }
      //       if (label.equals("Wing Area")) {
      //         plot_type = PLOT_TYPE_WING_AREA;
      //         pl8.setBackground(Color.yellow);
      //       }
      //       if (label.equals("Density")) {
      //         plot_type = PLOT_TYPE_DENSITY;
      //         pl9.setBackground(Color.yellow);
      //       }
      // 
      //       out.plot.loadPlot();
      // 
      //     }
      // 
      //   }  // FoilCtrls
      // 
      //   class BallCtrls extends Panel {
      //     FoilBoard app;
      //     JLabel l2;
      // 
      //     BallCtrls(FoilBoard target) {
      //       app = target;
      //       setLayout(new GridLayout(1,1,5,5));
      // 
      //       l2 = new JLabel(" ", JLabel.RIGHT);
      // 
      //       add(l2);
      //     }
      //   }  // BallCtrls
      // } // Lower
    }  // PlotSelectorTab

    class Anl extends InputPanel {
      FoilBoard app;

      // JLabel l1,l2,l3,l4,l5,l6;

      // Button bt1,bt2,bt3,bt4_1,bt4_2,bt5,bt6,bt7,bt8,bt9,bt10, bt_stab_aoa;
      //Button foilsim_drag, aquila_9p3_drag, naca4digit_drag;
      // int foil_drag_comp_method = DRAG_COMP_NACA4SERIES;
      //Button cbt1,cbt2,cbt3;

      JCheckBox chb_stab_aoa_corr;

      Anl (FoilBoard target) {

        app = target;
        setLayout(new GridLayout(8,1,5,5));

        JCheckBox chb; 
        add(chb = new JCheckBox("Aspect Ratio Correction for Lift", ar_lift_corr));
        chb.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) {             
          ar_lift_corr = e.getStateChange() == ItemEvent.SELECTED;
          computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
        }});

        add(chb = new JCheckBox("Compute Lift-Induced Drag", induced_drag_on));
        chb.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) {             
          induced_drag_on = e.getStateChange() == ItemEvent.SELECTED;
          computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
        }});

        add(chb = new JCheckBox("Reynolds Number Correction for Lift", re_corr));
        chb.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) {             
          re_corr = e.getStateChange() == ItemEvent.SELECTED;
          computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
        }});

        add(chb_stab_aoa_corr = new JCheckBox("Front Wing Downwash Correction for Back wing eff angle", stab_aoa_correction));
        chb_stab_aoa_corr.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent e) { 
          stab_aoa_correction = e.getStateChange() == ItemEvent.SELECTED;
          if (stab_aoa_correction) {
            // check if makes sense
            if (stab.xpos + stab.chord_xoffs <= wing.xpos + wing.chord_xoffs) {
              System.out.println("-- stab is not in wing downwash, no corretion needed");
              stab_aoa_correction = false;
              chb_stab_aoa_corr.setSelected(stab_aoa_correction);
            } 
          }
          computeFlowAndRegenPlotAndAdjust();
          con.recomp_all_parts();
        }});

        add(chb = new JCheckBox("Trace VPP goal finding ", false));
        chb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
              vpp.trace = e.getStateChange() == ItemEvent.SELECTED;
              System.out.println("-- trace: " + vpp.trace);
            }
          });

      }

      // Anl.loadPanel
      //
      // controled by a few flag varaibles
      @Override
      public void loadPanel () {
        out.plot.loadPlot();

        // Q: should do setState on the checkboxes?
        // A: only if flags were changed programatically without doing that,,,,

        // old ways....
        //
        // switch (stall_model_type) {
        // case STALL_MODEL_IDEAL_FLOW: 
        //   bt3.setBackground(Color.yellow);
        //   bt4_1.setBackground(Color.white);
        //   bt4_2.setBackground(Color.white);
        //   break;
        // case STALL_MODEL_DFLT: 
        //   bt3.setBackground(Color.white);
        //   bt4_2.setBackground(Color.white);
        //   bt4_1.setBackground(Color.yellow);
        //   break;
        // case STALL_MODEL_REFINED: 
        //   bt3.setBackground(Color.white);
        //   bt4_1.setBackground(Color.white);
        //   bt4_2.setBackground(Color.yellow);
        //   break;
        // }
        // if (ar_lift_corr) {
        //   bt6.setBackground(Color.white);
        //   bt5.setBackground(Color.yellow);
        // } else {
        //   bt5.setBackground(Color.white);
        //   bt6.setBackground(Color.yellow);
        // }
        // if (induced_drag_on) {
        //   bt8.setBackground(Color.white);
        //   bt7.setBackground(Color.yellow);
        // } else {
        //   bt7.setBackground(Color.white);
        //   bt8.setBackground(Color.yellow);
        // }
        // if (re_corr) {
        //   bt10.setBackground(Color.white);
        //   bt9.setBackground(Color.yellow);
        // } else {
        //   bt9.setBackground(Color.white);
        //   bt10.setBackground(Color.yellow);
        // }
        // switch (bdragflag) {
        // case 1: 
        //   cbt1.setBackground(Color.yellow);
        //   cbt2.setBackground(Color.white);
        //   cbt3.setBackground(Color.white);
        //   break;
        // case 2:
        //   cbt2.setBackground(Color.yellow);
        //   cbt1.setBackground(Color.white);
        //   cbt3.setBackground(Color.white);
        //   break;
        // case 3:
        //   cbt3.setBackground(Color.yellow);
        //   cbt2.setBackground(Color.white);
        //   cbt1.setBackground(Color.white);
        //   break;
        // }
        // if (stab_aoa_correction) 
        //   bt_stab_aoa.setBackground(Color.yellow);
        // else
        //   bt_stab_aoa.setBackground(Color.white);

        // do nto do this, ause stack overflow
        // computeFlowAndRegenPlotAndAdjust();
        // con.recomp_all_parts();
      }

      public boolean action (Event evt, Object arg) {
        System.out.println("-- warning: obsolete action() invoked... arg: " + arg);
        return false;
      } // Handler

      // public void handleBut(Event evt, Object arg) {
      //   String label = (String)arg;
      //      
      //   if (label.equals("Ideal Flow")) {
      //     stall_model_type = STALL_MODEL_IDEAL_FLOW;
      //     bt3.setBackground(Color.yellow);
      //     bt4_1.setBackground(Color.white);
      //     bt4_2.setBackground(Color.white);
      //   } else if (label.equals("Simple")) {
      //     stall_model_type = STALL_MODEL_DFLT;
      //     bt3.setBackground(Color.white);
      //     bt4_2.setBackground(Color.white);
      //     bt4_1.setBackground(Color.yellow);
      //   } else if (label.equals("Refined")) {
      //     stall_model_type = STALL_MODEL_REFINED;
      //     bt3.setBackground(Color.white);
      //     bt4_1.setBackground(Color.white);
      //     bt4_2.setBackground(Color.yellow);
      //   } else if (label.equals("AR On")) {
      //     ar_lift_corr = 1;
      //     bt6.setBackground(Color.white);
      //     bt5.setBackground(Color.yellow);
      //   } else if (label.equals("AR Off")) {
      //     ar_lift_corr = 0;
      //     bt5.setBackground(Color.white);
      //     bt6.setBackground(Color.yellow);
      //   } else if (label.equals("ID On")) {
      //     induced_drag_on = true;
      //     bt8.setBackground(Color.white);
      //     bt7.setBackground(Color.yellow);
      //   } else if (label.equals("ID Off")) {
      //     induced_drag_on = false;
      //     bt7.setBackground(Color.white);
      //     bt8.setBackground(Color.yellow);
      //   } else if (label.equals("Re On")) {
      //     re_corr = 1;
      //     bt10.setBackground(Color.white);
      //     bt9.setBackground(Color.yellow);
      //   } else if (label.equals("Re Off")) {
      //     re_corr = 0;
      //     bt9.setBackground(Color.white);
      //     bt10.setBackground(Color.yellow);
      //   } else if (label.equals("Smooth Ball")) {
      //     bdragflag = 1;
      //     cbt1.setBackground(Color.yellow);
      //     cbt2.setBackground(Color.white);
      //     cbt3.setBackground(Color.white);
      //   } else if (label.equals("Rough Ball")) {
      //     bdragflag = 2;
      //     cbt2.setBackground(Color.yellow);
      //     cbt1.setBackground(Color.white);
      //     cbt3.setBackground(Color.white);
      //   } else if (label.equals("Golf Ball")) {
      //     bdragflag = 3;
      //     cbt3.setBackground(Color.yellow);
      //     cbt2.setBackground(Color.white);
      //     cbt1.setBackground(Color.white);
      //   } else if (label.equals("Stab")) {
      //     stab_aoa_correction = !stab_aoa_correction;
      //     if (stab_aoa_correction) {
      //       // check if makes sense
      //       if (stab.xpos + stab.chord_xoffs <= wing.xpos + wing.chord_xoffs) {
      //         System.out.println("-- stab is not in wing downwash, no corretion needed");
      //         stab_aoa_correction = false;
      //       } else
      //         bt_stab_aoa.setBackground(Color.yellow);
      //     } else 
      //       bt_stab_aoa.setBackground(Color.white);
      //   } 
      // 
      //   // else if (label.equals("NACA*4** foils")) {
      //   //   //foil_drag_comp_method = DRAG_COMP_NACA4SERIES;
      //   //   naca4digit_drag.setBackground(Color.yellow);
      //   //   foilsim_drag.setBackground(Color.white);
      //   //   aquila_9p3_drag.setBackground(Color.white);
      //   // } else if (label.equals("Polynomial")) {
      //   //   //foil_drag_comp_method = DRAG_COMP_POLY_FOILSIMORIG;
      //   //   foilsim_drag.setBackground(Color.yellow);
      //   //   naca4digit_drag.setBackground(Color.white);
      //   //   aquila_9p3_drag.setBackground(Color.white);
      //   // } else if (label.startsWith("Aquila")) {
      //   //   //foil_drag_comp_method = DRAG_COMP_AQUILA_9P3;
      //   //   aquila_9p3_drag.setBackground(Color.yellow);
      //   //   foilsim_drag.setBackground(Color.white);
      //   //   naca4digit_drag.setBackground(Color.white);
      //   // }
      // 
      //   computeFlowAndRegenPlotAndAdjust();
      //   con.recomp_all_parts();
      // }
    } // end Anl
  }  // In 

  
  class Out extends 
           // Panel {
           javax.swing.JTabbedPane {
    FoilBoard app;
    Plot plot;
    Probe probe;
    Geometry geometry;
    Data data;
    PerfWeb perfweb;
    PerfWebSrc perfwebsrc;

    double ball_spin_angle;
    Viewer viewer;

    Out (FoilBoard target) { 
      app = target;
      viewer = new Viewer(app);

      // layout = new CardLayout();
      // setLayout(layout);

      plot = new Plot(app);
      probe = new Probe(app);
      geometry = new Geometry(app);
      data = new Data(app);
      

      addTab("Plot", null, plot, "Show Plot");
      addTab("Probe", null, probe, "Fluid Flow Probe Display");
      addTab("Geometry", null, geometry, "Hydrofoil components geometry,\nfoil/profile geometry tables or links");
      addTab("Data", null, data, "Hydrofoil components data,\nincluding size, shape etc");

      // TODO: regen this on update (if active)
      perfweb = new PerfWeb(app);
      addTab("Summary", null, perfweb, "Shape and Performance Summary");

      // phasin out this.... (stil works). instead, copy&past "summary" tab into
      // html tool such as https://html-online.com/editor/ and done!
      //
      // perfwebsrc = new PerfWebSrc(app);
      // addTab("<h1>Summary...", null, perfwebsrc, "Shape and Performance Summary,\n html codes for embedding");

      setSelectedComponent(perfweb);

      // adding actions to tabs: following "whoa" ideas from stackoverflow work but I use
      // overloaded paint() instead, seem simple.

      // JLabel label = new JLabel("XXX");
      // label.addMouseListener(new java.awt.event.MouseAdapter() {
      //   public void mouseClicked(MouseEvent e) {
      //     System.out.println("woah! ");
      //     setSelectedIndex(0);
      //   }});
      // 
      // setTabComponentAt(0, label);

      //setModel(new javax.swing.DefaultSingleSelectionModel() {
      //
      //    @Override
      //    public void setSelectedIndex(int index) {
      //      System.out.println("woah! " + index);
      //      setSelectedIndex(index);
      //    }
      //  });

      // can be used to attach aux logic here..
      this.addChangeListener(new javax.swing.event.ChangeListener() {
                    @Override
                    public void stateChanged(javax.swing.event.ChangeEvent e) {
                      // e.getSource();
                      // System.out.println("-- e: " + e);
                      //System.out.println("Selected paneNo : " + .getSelectedIndex());
                    } });
    }


    class Viewer extends Canvas implements Runnable {
      FoilBoard app;
      Thread runner;
      Point locate,anchor;
      long animation_count;
      boolean color_flip;

      Viewer (FoilBoard target) {
        setBackground(color_very_dark);
        runner = null;
      } 

      public Insets insets() {
        return new Insets(0,10,0,10);
      }

      boolean zoom_widget_active = false;
      boolean force_scale_widget_active = false;
      boolean mesh_x_angle_widget_active = false;
      boolean mesh_z_angle_widget_active = false;

      double force_scale = 0.001;
      int force_scale_slider = 50;
      int mesh_x_angle_slider = 50;
      int mesh_z_angle_slider = 50;
      double  mesh_x_angle  = 0, mesh_x_angle_on_press = 0;
      double  mesh_z_angle = 0, mesh_z_angle_on_press = 0;

      public boolean mouseDown(Event evt, int x, int y) {
        anchor = new Point(x,y);
        if (y >= 30 && y <= 165) {
          if (x < 30)
            zoom_widget_active = true;
          else {
            if (viewflg == VIEW_FORCES && x < 60)
              force_scale_widget_active = true;
            else if (viewflg == VIEW_3D_MESH) {
              // if (x < 60)
              //   mesh_x_angle_widget_active = true;
              // else if (x < 90)
              //   mesh_z_angle_widget_active = true;
              mesh_x_angle_on_press = mesh_x_angle;
              mesh_z_angle_on_press = mesh_z_angle;
            }
          }
        }
        // no! handleButton(x,y);
        return true;
      }

      public boolean mouseUp(Event evt, int x, int y) {
        handleButton(x,y);
        zoom_widget_active = false;
        force_scale_widget_active = false;
        mesh_x_angle_widget_active = mesh_z_angle_widget_active = false;
        return true;
      }

      public boolean mouseDrag(Event evt, int x, int y) {
        handle(x,y);
        return true;
      }


      public void handle(int x, int y) {
        if (zoom_widget_active) {  // adjust zoom widget
          zoom_slider_pos_y = y;
          if (zoom_slider_pos_y < 30) zoom_slider_pos_y = 30;
          if (zoom_slider_pos_y > 165) zoom_slider_pos_y = 165;
          fact = 10.0 + (zoom_slider_pos_y-30)*1.0;
          current_part.spanfac = (int)(2.0*fact*current_part.aspect_rat*.3535);
          xt1 = xt + current_part.spanfac;
          yt1 = yt - current_part.spanfac;
          xt2 = xt - current_part.spanfac;
          yt2 = yt + current_part.spanfac;
        } else if (force_scale_widget_active) { // force scale widget
          force_scale_slider = y;
          if (force_scale_slider < 30) force_scale_slider = 30;
          else if (force_scale_slider > 165) force_scale_slider = 165;
          force_scale = 0.0005 + 0.0001*(force_scale_slider-30);
        // } else if (mesh_x_angle_widget_active) { 
        //   mesh_x_angle_slider = Math.max(30,Math.min(165,y));
        //   mesh_x_angle = -180 + 360*(mesh_x_angle_slider-30)/135;
        //   //System.out.println("-- mesh_x_angle: " + mesh_x_angle);
        // } else if (mesh_z_angle_widget_active) { 
        //   mesh_z_angle_slider = Math.max(30,Math.min(165,y));
        //   mesh_z_angle = -180 + 360*(mesh_z_angle_slider-30)/135;
        //   //System.out.println("-- mesh_z_angle: " + mesh_z_angle);
        } else if (viewflg == VIEW_3D_MESH) {
          mesh_x_angle = mesh_x_angle_on_press - 1*(y - anchor.y);
          mesh_z_angle = mesh_z_angle_on_press - 1*(x - anchor.x);
          
        } else {   // translate or force zoom
          if (displ == DISPLAY_DIRECTION)  { // move the rake
            locate = new Point(x,y);
            xflow = xflow + 1*(locate.x - anchor.x);
            if (xflow < -10.0) xflow = -10.0;
            if (xflow > 0.0) xflow = 0.0;
            computeFlowAndRegenPlot();
          } else {
            locate = new Point(x,y);
            yt =  yt + (int) (.1*(locate.y - anchor.y));
            xt =  xt + (int) (.1*(locate.x - anchor.x));
            if (xt > 320) xt = 320;
            if (xt < -280) xt = -280;
            if (yt > 300) yt = 300;
            if (yt <-300) yt = -300;
            xt1 = xt + current_part.spanfac;
            yt1 = yt - current_part.spanfac;
            xt2 = xt - current_part.spanfac;
            yt2 = yt + current_part.spanfac;
          } 
        }
      }
    
      void find_it() {
        // System.out.println("-- centering,zooming: " + fact);
        xt = 210;  yt = 105; fact = 50.0;
        if (viewflg != VIEW_FORCES) zoom_slider_pos_y = 50;
        current_part.spanfac = (int)(2.0*fact*current_part.aspect_rat*.3535);
        xt1 = xt + current_part.spanfac;
        yt1 = yt - current_part.spanfac;
        xt2 = xt - current_part.spanfac;
        yt2 = yt + current_part.spanfac;
      
      }

      public void handleButton (int x, int y) {
        if (y < 15) { 
          if (x >= 90 && x <= 139) {   //edge view
            viewflg = VIEW_EDGE;
          }
          if (x >= 140 && x <= 170) {   //forces view
            if (current_part.foil != FOIL_CYLINDER && current_part.foil != FOIL_BALL) {
              viewflg = VIEW_FORCES;
              // thic collides with running cg pos plot....
              //vpp.steady_flight_at_given_speed(5, 0);
            } else 
              viewflg = VIEW_EDGE;
            displ = 3;
            pboflag = 0;
          }
          if (x >= 171 && x <= 239) {   // 3d mesh view
            if (current_part.foil != FOIL_CYLINDER && current_part.foil != FOIL_BALL)
              viewflg = VIEW_3D_MESH;
            else 
              viewflg = VIEW_EDGE;
          }
          if (x >= 240 && x <= 270) {   //find
            find_it();
          }
        } else if (y <= 30) { 
          if (x >= 80 && x <= 154) {   //display streamlines or orthogonall view or toggle force labels
            if (viewflg == VIEW_3D_MESH) perspective = false;
            else if (viewflg == VIEW_FORCES) force_labels = !force_labels;
            else displ = DISPLAY_STREAMLINES;
          }
          if (x >= 155 && x <= 204) {   //display animation
            if (viewflg == VIEW_3D_MESH) perspective = true;
            else if (viewflg != VIEW_FORCES) displ = DISPLAY_ANIMATION;
          }
          if (x >= 205 && x <= 249) {   //display direction
            if (viewflg != VIEW_FORCES) displ = DISPLAY_DIRECTION;
          }
          if (x >= 250 && x <= 330) {   //display geometry
            displ = DISPLAY_GEOMETRY;
            pboflag = 0;
          }
        }
        out.viewer.repaint();
      }

      public void start() {
        if (runner == null) {
          runner = new Thread(this);
          runner.start();
        }
        animation_count = 0;                              /* MODS  21 JUL 99 */
        color_flip = true;                              /* MODS  27 JUL 99 */
      }

      //    public void run() {
      //      int timer;
      // 
      //      timer = 100;
      //      while (true) {
      //        ++ animation_count;
      //        try { Thread.sleep(timer); }
      //        catch (InterruptedException e) {}
      //        out.viewer.repaint();
      //        if (animation_count == 3) {
      //          animation_count = 0;
      //          color_flip = !color_flip;               /* MODS 27 JUL 99 */
      //        }
      //        timer = 135 - (int) (.227 *velocity/vconv);
      //        // make the ball spin
      //        if (foil_is_cylinder_or_ball(current_part.foil)) {
      //          ball_spin_angle = ball_spin_angle + spin*spindr*5.;
      //          if (ball_spin_angle < -360.0) {
      //            ball_spin_angle = ball_spin_angle + 360.0;
      //          }
      //          if (ball_spin_angle > 360.0) {
      //            ball_spin_angle = ball_spin_angle - 360.0;
      //          }
      //        }
      //      }
      //    }
    
      int timer = 100;
      void run_step () {
        ++animation_count;
        viewer.repaint();
        if (animation_count == 3) {
          animation_count = 0;
          color_flip = !color_flip; 
        }

        timer = 135 - (int) (.227 * velocity / vconv);
        // make the ball spin
        if (foil_is_cylinder_or_ball(current_part.foil)) {
          ball_spin_angle = ball_spin_angle + spin * spindr * 5.;
          if (ball_spin_angle < -360.0) {
            ball_spin_angle = ball_spin_angle + 360.0;
          }
          if (ball_spin_angle > 360.0) {
            ball_spin_angle = ball_spin_angle - 360.0;
          }
        }
      }

      /**
       * @j2sNative
       * 
       *            this.run_step(); var self = this; setTimeout(function()
       *            {self.run();}, self.timer);
       */
      public void run () {
        while (true) {
          try {
            Thread.sleep(timer);
          } catch (InterruptedException e) {
          }
          run_step();
        }
      }


      public void update (Graphics g) {
        out.viewer.paint(g); // ?? is this just this.paint()??
      }

      int toInt (double val) {
        // System.out.println("-- val: " + val);
        return (int)val;
      }

      // rotation transform, see https://en.wikipedia.org/wiki/Rotation_matrix
      void to_screen_x_y(Point3D p, int[] x, int[] y, int i, double offx, double scalex, double offy, double scaley, int screen_off_x, int screen_off_y)  { 
        double rp = Math.toRadians(craft_pitch); 
        double space_x = scalex*(offx+p.x);
        double space_y = scaley*(offy+p.z);
        space_x = space_x*Math.cos(rp) - space_y*Math.sin(rp); 
        space_y = space_x*Math.sin(rp) + space_y*Math.cos(rp); 

        x[i] = screen_off_x + toInt(space_x);
        y[i] = screen_off_y + toInt(space_y);

      }

      void drawPart (Part p, double offx, double scalex, double offy, double scaley, int screen_off_x, int screen_off_y) {
        int i;
        int x[] = new int[8];
        int y[] = new int[8];

        off1Gg.setColor(color_very_dark);

        Point3D[] le = p.mesh_LE;
        Point3D[] te = p.mesh_TE;
        for (i = 0; i < le.length; i++) {
          to_screen_x_y(le[i],x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(te[i],x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          off1Gg.drawLine(x[0], y[0], x[1], y[1]);
        }
        
        for (i = 0; i < le.length-1; i++) {
          to_screen_x_y(le[i],x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(le[i+1],x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(te[i],x,y,3,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(te[i+1],x,y,2,offx,scalex,offy,scaley,screen_off_x,screen_off_y);

          off1Gg.setColor(color_very_dark);
          off1Gg.drawLine(x[0], y[0], x[1], y[1]);
          off1Gg.drawLine(x[2], y[2], x[3], y[3]);
          off1Gg.setColor(color_dark);
          off1Gg.fillPolygon(x, y, 4);
        }
      }

      boolean perspective = false, force_labels = false;

      double persp_scale (double z) {
        if (!perspective) return 1;
        double distance_to_screen_from_z0 = 1; // 1meter
        return 1/(distance_to_screen_from_z0+z);
      }

      void drawPart3D (Part p, double offx, double scalex, double offy, double scaley, int screen_off_x, int screen_off_y) {
        int i;
        int x[] = new int[8];
        int y[] = new int[8];

        off1Gg.setColor(Color.white);

        Point3D[] le0 = p.mesh_LE;
        Point3D[] te0 = p.mesh_TE;

        // calculated angles
        double x_rad = Math.toRadians(mesh_x_angle),
          y_rad = Math.toRadians(mesh_z_angle),
          z_rad = 0;
        double XSIN = Math.sin(x_rad), XCOS = Math.cos(x_rad), 
          YSIN = Math.sin(y_rad), YCOS = Math.cos(y_rad), 
          ZSIN = Math.sin(z_rad), ZCOS = Math.cos(z_rad);

        // Rotation Matrix
        double RMX1 = YCOS*ZCOS, RMY1 =-ZSIN * YCOS, RMZ1 = YSIN,
          RMX2 =ZCOS * -YSIN * -XSIN + ZSIN * XCOS, RMY2 =-ZSIN * -YSIN * -XSIN + ZCOS*XCOS, RMZ2 =YCOS * -XSIN,
          RMX3 =ZCOS * -YSIN * XCOS + ZSIN * XSIN, RMY3 =-ZSIN * -YSIN * XCOS + ZCOS * XSIN, RMZ3 =YCOS * XCOS;

        Point3D[] le =   new Point3D[p.mesh_LE.length];
        Point3D[] te =   new Point3D[p.mesh_TE.length];
        Point3D[] top1 = new Point3D[p.mesh_TE.length];
        Point3D[] bot1 = new Point3D[p.mesh_TE.length];

        // here we swap y and z extracting them from le0 and te0 (see sail-3d)
        // and swap them in le te too (see to_screen_x_y)
        for (i = 0; i < le.length; i++) {
          Point3D v = le0[i]; // 'v' is mesh vertex
          double z =((offx+v.x)*RMX3)+((offy+v.z)*RMY3)+(v.y*RMZ3);
          double persp = persp_scale(z);
          le[i] = new Point3D(persp*(((offx+v.x)*RMX1)+((offy+v.z)*RMY1)+(v.y*RMZ1)),
                              z,
                              persp*(((offx+v.x)*RMX2)+((offy+v.z)*RMY2)+(v.y*RMZ2)));
          v = te0[i];
          z =((offx+v.x)*RMX3)+((offy+v.z)*RMY3)+(v.y*RMZ3);
          persp = persp_scale(z);
          te[i] = new Point3D(persp*(((offx+v.x)*RMX1)+((offy+v.z)*RMY1)+(v.y*RMZ1)),
                              z,
                              persp*(((offx+v.x)*RMX2)+((offy+v.z)*RMY2)+(v.y*RMZ2)));
          // we now fill 6 intermediates
          double thickness = p.thickness*0.005*(te0[i].x-le0[i].x);
          double camber    = p.camber   *0.005*(te0[i].x-le0[i].x);
          if (p != strut) {
            v = new Point3D(le0[i].x+0.25*(te0[i].x-le0[i].x), le0[i].y, le0[i].z+thickness+camber);
            z =((offx+v.x)*RMX3)+((offy+v.z)*RMY3)+(v.y*RMZ3);
            persp = persp_scale(z);
            top1[i] = new Point3D(persp*(((offx+v.x)*RMX1)+((offy+v.z)*RMY1)+(v.y*RMZ1)),
                                  z,
                                  persp*(((offx+v.x)*RMX2)+((offy+v.z)*RMY2)+(v.y*RMZ2)));
            v = new Point3D(le0[i].x+0.25*(te0[i].x-le0[i].x), le0[i].y, le0[i].z-thickness+camber);
            z =((offx+v.x)*RMX3)+((offy+v.z)*RMY3)+(v.y*RMZ3);
            persp = persp_scale(z);
            bot1[i] = new Point3D(persp*(((offx+v.x)*RMX1)+((offy+v.z)*RMY1)+(v.y*RMZ1)),
                                  z,
                                  persp*(((offx+v.x)*RMX2)+((offy+v.z)*RMY2)+(v.y*RMZ2)));
          } else {
            v = new Point3D(le0[i].x+0.25*(te0[i].x-le0[i].x), le0[i].y+thickness, le0[i].z);
            z =((offx+v.x)*RMX3)+((offy+v.z)*RMY3)+(v.y*RMZ3);
            persp = persp_scale(z);
            top1[i] = new Point3D(persp*(((offx+v.x)*RMX1)+((offy+v.z)*RMY1)+(v.y*RMZ1)),
                                  z,
                                  persp*(((offx+v.x)*RMX2)+((offy+v.z)*RMY2)+(v.y*RMZ2)));
            v = new Point3D(le0[i].x+0.25*(te0[i].x-le0[i].x), le0[i].y-thickness, le0[i].z);
            z =((offx+v.x)*RMX3)+((offy+v.z)*RMY3)+(v.y*RMZ3);
            persp = persp_scale(z);
            bot1[i] = new Point3D(persp*(((offx+v.x)*RMX1)+((offy+v.z)*RMY1)+(v.y*RMZ1)),
                                  z,
                                  persp*(((offx+v.x)*RMX2)+((offy+v.z)*RMY2)+(v.y*RMZ2)));
          }
        }

        /// do not need them for the remaining
        offx = offy = 0;
        
        for (i = 0; i < le.length; i++) {
          to_screen_x_y( le[i],x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(top1[i],x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y( te[i],x,y,2,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(bot1[i],x,y,3,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          off1Gg.drawLine(x[0], y[0], x[1], y[1]);
          off1Gg.drawLine(x[1], y[1], x[2], y[2]);
          off1Gg.drawLine(x[2], y[2], x[3], y[3]);
          off1Gg.drawLine(x[3], y[3], x[0], y[0]);
        }
        
        for (i = 0; i < le.length-1; i++) {
          to_screen_x_y(le[i],x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(le[i+1],x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(te[i],x,y,3,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(te[i+1],x,y,2,offx,scalex,offy,scaley,screen_off_x,screen_off_y);

          // off1Gg.setColor(Color.white);
          //off1Gg.drawPolygon(x, y, 4);
          off1Gg.drawLine(x[0], y[0], x[1], y[1]);
          off1Gg.drawLine(x[2], y[2], x[3], y[3]);
          //off1Gg.setColor(Color.gray);
          //off1Gg.fillPolygon(x, y, 4);
        }
      }


      void drawVectorVert (Color color, String label, int start_x, int start_y, int end_y) {
        int x[] = new int[3];
        int y[] = new int[3];

        if (Math.abs(start_y - end_y) < 5) // too small, do not draw
          return;

        off1Gg.setColor(color);
        if (force_labels) {
          if (start_y > end_y) // pointing up, draw on the left
            off1Gg.drawString(label, start_x - 5 - 6*label.length(), end_y + 20);
          else // pointing down
            off1Gg.drawString(label, start_x + 5, end_y - 20);
        }
        off1Gg.drawLine(start_x, start_y, start_x, end_y);
        if (start_y <  end_y - 7) {
          x[0] = start_x-5;  x[1] = start_x+5; x[2] = start_x;
          y[0] = end_y - 10;  y[1] = end_y - 10; y[2] = end_y;
        } else if (start_y > end_y + 10 ) {
          x[0] = start_x-5;  x[1] = start_x+5; x[2] = start_x;
          y[0] = end_y + 10;  y[1] = end_y + 10; y[2] = end_y;
        }
          off1Gg.fillPolygon(x,y,3);
      }
 
      void drawVectorHoriz (Color color, String label, int start_y, int start_x, int end_x) {
        int x[] = new int[3];
        int y[] = new int[3];

        if (Math.abs(start_x - end_x) < 5) // too small, do not draw
          return;

        off1Gg.setColor(color);
        if (force_labels) {
          if (false) 
            // below
            off1Gg.drawString(label, (start_x+end_x)/2, start_y + 15);
          else // right
            off1Gg.drawString(label, Math.max(start_x,end_x) + 4, start_y+5);
        }
        off1Gg.drawLine(start_x, start_y, end_x, start_y);
        if (start_x <  end_x - 7) {
          y[0] = start_y-5;  y[1] = start_y+5; y[2] = start_y;
          x[0] = end_x - 10;  x[1] = end_x - 10; x[2] = end_x;
        } else if (start_x > end_x + 10 ) {
          y[0] = start_y-5;  y[1] = start_y+5; y[2] = start_y;
          x[0] = end_x + 10;  x[1] = end_x + 10; x[2] = end_x;
        }
          off1Gg.fillPolygon(x,y,3);
      }

      void drawSliderWidget (String label, int x_offset, int hand_pos, boolean active_p) {
          off1Gg.setColor(active_p ? Color.white : Color.green);
          off1Gg.drawString(label,x_offset+2,182);
          off1Gg.drawLine(x_offset+15,30,x_offset+15,165);
          // frame 
          // not now off1Gg.drawRect(30+3,26,24,144);
          off1Gg.fillRect(x_offset+5,hand_pos-3,20,6);
      }
      
      // class Viewer
      public void paint (Graphics g) {
        int i,j,k,n;
        //System.out.println("-- paint: ");
        int xlabel,ylabel,ind,inmax,inmin;
        int x[] = new int[40];
        int y[] = new int[40];
        double offx,scalex,offy,scaley,waste,incy,incx;
        double xl,yl,slope,radvec,xvec,yvec;
        int camb_x[] = new int[30]; // was 19, we need 30 for imported data
        int camb_y[] = new int[30]; // was 19, we need 30 for imported data
        Color col, col_bg;

        Foil f = current_part.foil;

        int panel_height = getHeight();
        int panel_width = getWidth();
        // System.out.println("-- panel_height: " + panel_height);

        col = new Color(0,0,0);
        if (planet == 0) col = Color.cyan;
        if (planet == 1) col = Color.orange;
        if (planet == 2) col = Color.green;
        if (planet >= 3) col = Color.cyan;

        if (viewflg == VIEW_FORCES) { // 3D View with force vectors
          // sky/air
          // col_bg = color_sky_blue_light;
          col_bg = Color.BLUE;
          off1Gg.setColor(col_bg);
          off1Gg.fillRect(0,0,panel_width, panel_height);

          int screen_off_x = panel_width/2 + 40, screen_off_y = (int)(0.7*panel_height); // is at waterline

          // water
          // off1Gg.setColor(Color.cyan);
          off1Gg.setColor(color_water_dark);
          off1Gg.fillRect(0, screen_off_y,panel_width,(int)(0.3*panel_height)+1);

          // 150 pixels is 1.5m
          scalex = zoom_slider_pos_y; scaley = -zoom_slider_pos_y;
          offx = 0; // maybe -strut.xpos; 
          double flight_height = strut.span * (alt/100); // board bottom above water
          offy =  flight_height - strut.span; // this is flight depth
          
          drawPart(strut,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawPart( fuse,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawPart( wing,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawPart( stab,offx,scalex,offy,scaley,screen_off_x,screen_off_y);

          // board profile
          //x[0] = screen_off_x+toInt(scalex*(offx + - 0.9));
          //y[0] = screen_off_y+toInt(scaley*(offy + strut.span));

          to_screen_x_y(new Point3D(-BOARD_LENGTH+mast_xpos+MAST_LE_TO_TRANSOM+0.3,0,strut.span),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          //x[1] = screen_off_x+toInt(scalex*(offx + -1.2));
          //y[1] = screen_off_y+toInt(scaley*(offy + strut.span + BOARD_THICKNESS));
          to_screen_x_y(new Point3D(-BOARD_LENGTH+mast_xpos+MAST_LE_TO_TRANSOM,0,strut.span + BOARD_THICKNESS),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          //x[2] = screen_off_x+toInt(scalex*(offx + strut.xpos + 0.45));
          //y[2] = screen_off_y+toInt(scaley*(offy + strut.span + BOARD_THICKNESS));
          to_screen_x_y(new Point3D(mast_xpos + MAST_LE_TO_TRANSOM,0,strut.span + BOARD_THICKNESS),x,y,2,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          //x[3] = screen_off_x+toInt(scalex*(offx + strut.xpos + 0.45));
          //y[3] = screen_off_y+toInt(scaley*(offy + strut.span));
          to_screen_x_y(new Point3D(mast_xpos + MAST_LE_TO_TRANSOM,0,strut.span),x,y,3,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          off1Gg.setColor(Color.white);
          off1Gg.fillPolygon(x,y,4);

          // position related to strut bottom LE
          double cg_x_pos = strut.xpos - con.cg_pos; // note minus in front of con.cg_pos !!!
          to_screen_x_y(new Point3D(cg_x_pos, // just for appearance
                                    0,strut.span+BOARD_THICKNESS),x,y,0,offx,scalex,offy,scaley,0,0);
          
          double cg_x_pos_rot = x[0]/scalex; // after rotation, we want cg be at level distance from board surface (related to rider height)
          // feet-mast load distribution if any
          // note minus in front of con.cg_pos !!!
          // -cg_pos = k*moff + (1-k)*rx -> rx = (-cgpos - k*l)/(1-k)
          //double rider_center_x = strut.xpos + (-con.cg_pos + mast_foot_pressure_k * mast_foot_dist_from_strut_le)/(1-mast_foot_pressure_k);
          double rider_center_x = cg_x_pos;
          to_screen_x_y(new Point3D(rider_center_x, // just for appearance
                                    0,strut.span+BOARD_THICKNESS),x,y,0,offx,scalex,offy,scaley,0,0);
          double rider_rot_center_x_real = x[0]/scalex; // rotation corrected, real coords not screen
          double rider_rot_center_x = rider_rot_center_x_real - 0.04; // rotation corrected, real coords not screen
          double rider_rot_center_y = y[0]/scaley; // rotation corrected, real coords not screen
          double rider_drive_center_y = rider_rot_center_y + RIDER_DRIVE_HEIGHT; // rotation corrected, real coords not screen

          // draw mast boom sail or kite lines
          if (craft_type == KITEFOIL) {
            // bar and 3 lines go at angle
            i = 0;
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55));
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 300));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 300));

            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 0.4));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 0.4));

            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 300));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 300));
            off1Gg.setColor(Color.WHITE);
            //off1Gg.drawPolygon(x,y,i);

            i = 0;
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55));
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 + 0.2));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 0.2));
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 280));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 320));

            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 0.4+0.01));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 0.4+0.01));

            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 + 0.4+0.01));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 - 0.4+0.01));

            off1Gg.drawPolygon(x,y,i);
            
            i = 0;
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55));
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 0.2));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 - 0.2));
            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 320));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 280));

            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 - 0.4 - 0.01));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 + 0.4 - 0.01));

            x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.54 + 0.4 - 0.01));
            y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.55 - 0.4- 0.01));

            off1Gg.drawPolygon(x,y,i);

          } else {
            // this can be computed once....
            // 1 WS mast is an arc, see https://stackoverflow.com/questions/4196749/draw-arc-with-2-points-and-center-of-the-circle
            // double ws_mast_arch_0x = 5.0; // pretty arb...
            // double ws_mast_arch_0y = strut.span;
            // double ws_mast_base_x  = mast_xpos-WS_MASTBASE_MAST_LE;
            // double ws_mast_base_y  = strut.span+BOARD_THICKNESS;
            // double ws_mast_tip_x  = 0.55;
            // double ws_mast_tip_y  = ws_mast_base_y + 4.2;
            // double ws_mast_r = Math.sqrt((ws_mast_base_x - ws_mast_arch_0x)*(ws_mast_base_x - ws_mast_arch_0x) + (ws_mast_base_y - ws_mast_arch_0y)*(ws_mast_base_y - ws_mast_arch_0y)); 
            // // these will be translated
            // double ws_arc_x = ws_mast_base_x - ws_mast_r;
            // double ws_arc_y = ws_mast_base_y - ws_mast_r;
            // to_screen_x_y(new Point3D(ws_arc_x,0,ws_arc_y),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
            // double arc_wh = 2*ws_mast_r;
            // to_screen_x_y(new Point3D(arc_wh,0,arc_wh),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
            // double arc_start = (180/Math.PI*Math.atan2((ws_mast_base_y - ws_mast_arch_0y), (ws_mast_base_x - ws_mast_arch_0x)));
            // double arc_stop  = (180/Math.PI*Math.atan2((ws_mast_tip_y  - ws_mast_arch_0y), (ws_mast_tip_x  - ws_mast_arch_0x)));
            // to_screen_x_y(new Point3D(arc_start,0,arc_start),x,y,2,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
            // off1Gg.drawArc(x[0], y[0], x[1], y[1], x[2], y[2]);
            // System.out.println("-- x: " + java.util.Arrays.toString(x));
            // System.out.println("-- y: " + java.util.Arrays.toString(y));

            // base
            double wsmb_x  = mast_xpos-WS_MASTBASE_MAST_LE-0.025;
            double wsmb_y  = strut.span+BOARD_THICKNESS+0.1;

            to_screen_x_y(new Point3D(wsmb_x,0, wsmb_y),x,y,0,offx,scalex,offy,scaley,0,0);
            wsmb_x = x[0]/scalex;
            wsmb_y = y[0]/scaley; // rotation corrected, real coords not screen
            i = 0;

            // this is generic cyan colored sail, a bit big
            //
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.15));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.9));
            // // boom pt1
            // int boom_idx_1 = i;
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.28));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.4));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.6));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+2.3));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.0));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.1));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.3));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.6));
            // 
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.5));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.9));
            // 
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.8));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.6));
            // 
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.96));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.0));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.95));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+2.2));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.78));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.3));
            // int boom_idx_2 = i;
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.67));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.66));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.4));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.4));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.9));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.21));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.9));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.21));
            // x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.6));
            // y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.13));
            // 
            // off1Gg.fillPolygon(x,y,i);
            // off1Gg.setColor(Color.CYAN);
            // off1Gg.fillPolygon(x,y,i);
            // off1Gg.setColor(Color.BLACK);
            // off1Gg.drawPolygon(x,y,i);
            // off1Gg.setColor(Color.BLACK);
            // off1Gg.drawLine(x[boom_idx_1]+2, y[boom_idx_1]+2, x[boom_idx_2]+2, y[boom_idx_2]+2);
            // off1Gg.drawLine(x[boom_idx_1]-2, y[boom_idx_1]-2, x[boom_idx_2]-2, y[boom_idx_2]-2);


            // goya sail 4.5-4.7
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.05));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.5));

            int batten1_1 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.095));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.83));

            // boom pt1
            int boom_idx_1 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.275));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.51));

            int batten2_1 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.4));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.85));

            int batten3_1 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.81));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+2.6));

            int batten4_1 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.25));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.33));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.6));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.9));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+2.22));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.53));

            int batten4_2 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+2.25));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+3.43));

            int batten3_2 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+2.16));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+2.575));

            int batten2_2 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+2.06));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.65));

            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.94));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.47));

            int boom_idx_2 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.72));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.72));
            int batten1_2 = i;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.6));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.55));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.15));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.15));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.05));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.0));
            off1Gg.setColor(color_light_cyan);
            off1Gg.fillPolygon(x,y,i);

            // outline, boom and battens
            off1Gg.setColor(Color.BLACK);
            off1Gg.drawPolygon(x,y,i);
            off1Gg.drawLine(x[batten1_1], y[batten1_1], x[batten1_2], y[batten1_2]);
            off1Gg.drawLine(x[batten2_1], y[batten2_1], x[batten2_2], y[batten2_2]);
            off1Gg.drawLine(x[batten3_1], y[batten3_1], x[batten3_2], y[batten3_2]);
            off1Gg.drawLine(x[batten4_1], y[batten4_1], x[batten4_2], y[batten4_2]);

            //off1Gg.drawLine(x[boom_idx_1]-2, y[boom_idx_1]-2, x[boom_idx_2]-2, y[boom_idx_2]-2);
            //off1Gg.drawLine(, , x[boom_idx_2]+2, y[boom_idx_2]+2);
            int thk = 1; 
            x[0] = x[boom_idx_1]+thk;
            y[0] = y[boom_idx_1]-thk;
            x[1] = x[boom_idx_1]-thk;
            y[1] = y[boom_idx_1]+thk;
            x[2] = x[boom_idx_2]-thk;
            y[2] = y[boom_idx_2]+thk;
            x[3] = x[boom_idx_2]+thk;
            y[3] = y[boom_idx_2]-thk;
            off1Gg.fillPolygon(x,y,4);            


            // window1
            i = 0;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.4));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.82));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.52));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.33));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.68));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.78));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.35));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.7));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.57));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.07));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.05));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+0.7));
            off1Gg.setColor(Color.BLUE);
            off1Gg.fillPolygon(x,y,i);

            // window2
            i = 0;
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+0.71));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.845));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.04));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+2.53));
            x[i  ] = screen_off_x+toInt(scalex*(wsmb_x+1.325));
            y[i++] = screen_off_y+toInt(scaley*(wsmb_y+1.77));
            off1Gg.setColor(Color.BLUE);
            off1Gg.fillPolygon(x,y,i);
            
          }
          

          // RIDER
          i = 0;

          // front foot
          to_screen_x_y(new Point3D(-0.1+strut.xpos - 0.7*con.cg_pos + 0.2*rider_center_x - 0.22,0,strut.span+BOARD_THICKNESS),x,y,i++,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D(-0.1+strut.xpos - 0.7*con.cg_pos + 0.2*rider_center_x - 0.32,0,strut.span+BOARD_THICKNESS),x,y,i++,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          // front leg.. etc
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.20));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 0.86));

          // thick hand
          // x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.20));
          // y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.4));
          // x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.50));

          // y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.4));
          // x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.50));
          // y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.6));
          // x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.15));
          // y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.6));
          // x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.15));


          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.20));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.34));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.45));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.11));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.58));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.5));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.5));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.6));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.42));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.35));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.20));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.6));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.15));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.6));
          // top head
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x - 0.13));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.82));

          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x + 0.13));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.82));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x + 0.15));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.6));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x + 0.23));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.6));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x + 0.23));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.4));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x + 0.20));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 1.35));
          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x + 0.20));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 0.86));

          // back foot
          to_screen_x_y(new Point3D(-0.1+strut.xpos - 0.5*con.cg_pos + 0.2*rider_center_x + 0.24,0,strut.span+ BOARD_THICKNESS),x,y,i++,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D(-0.1+strut.xpos - 0.5*con.cg_pos+  0.2*rider_center_x + 0.14,0,strut.span+ BOARD_THICKNESS),x,y,i++,offx,scalex,offy,scaley,screen_off_x,screen_off_y);

          x[i  ] = screen_off_x+toInt(scalex*(rider_rot_center_x));
          y[i++] = screen_off_y+toInt(scaley*(rider_rot_center_y-0.031 + 0.8));

          off1Gg.setColor(Color.yellow);
          off1Gg.fillPolygon(x,y,i);


          //off1Gg.setColor(Color.green);
          //off1Gg.drawString("Flow",45,145);
          //off1Gg.drawLine(40,155,40,125);
          //x[0] = 35;  x[1] = 45; x[2] = 40;
          //y[0] = 125;  y[1] = 125; y[2] = 115;
          //off1Gg.fillPolygon(x,y,3);
          double drag_x;

          to_screen_x_y(new Point3D(wing.xpos+wing.chord_xoffs+0.3*wing.chord,0,wing.chord_zoffs),                        x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          //         need offx here ^^^  ? or not?
          to_screen_x_y(new Point3D(wing.xpos+wing.chord_xoffs+0.3*wing.chord,0,wing.chord_zoffs + wing.lift*force_scale),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawVectorVert(wing.lift > 0 ? Color.green : Color.red, "Wing Lift", x[0], y[0], y[1]);

          to_screen_x_y(new Point3D(wing.xpos+wing.chord_xoffs+0.7*wing.chord,0,wing.chord_zoffs),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D(wing.xpos+wing.chord_xoffs+0.7*wing.chord + wing.drag*force_scale,0,wing.chord_zoffs),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawVectorHoriz(Color.red, "Wing Drag", y[0], x[0], x[1]);

          to_screen_x_y(new Point3D(stab.xpos+stab.chord_xoffs+0.3*stab.chord,0,stab.chord_zoffs),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D(stab.xpos+stab.chord_xoffs+0.3*stab.chord,0,stab.chord_zoffs + stab.lift*force_scale),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawVectorVert(stab.lift > 0 ? Color.green : Color.red, "Stab Lift", x[0], y[0], y[1]);

          drag_x = offx + stab.xpos + 0.5*stab.chord;
          to_screen_x_y(new Point3D(stab.xpos+stab.chord_xoffs+0.7*stab.chord,0,stab.chord_zoffs),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D(stab.xpos+stab.chord_xoffs+0.7*stab.chord+ stab.drag*force_scale,0,stab.chord_zoffs),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawVectorHoriz(Color.red, "Stab Drag", y[0], x[0], x[1]);


          // combined CG is a bit forward from rider CG
          drawVectorVert(Color.red, "Rider Weight", 
                         screen_off_x+toInt(scalex*(cg_x_pos_rot)),
                         screen_off_y+toInt(scaley*(rider_rot_center_y-0.031+1.0)),
                         screen_off_y+toInt(scaley*(rider_rot_center_y-0.031+1.0- 
                                                    // vpp.steady_flight_at_given_speed___load*force_scale
                                                    rider_weight*force_scale
                                                    )));

          // board weight.
          // board weight arm is 1/2 length minus mast LE to transom
          to_screen_x_y(new Point3D((-BOARD_LENGTH/2+mast_xpos+MAST_LE_TO_TRANSOM),0,strut.span+BOARD_THICKNESS/2),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D((-BOARD_LENGTH/2+mast_xpos+MAST_LE_TO_TRANSOM),0,strut.span+BOARD_THICKNESS/2-BOARD_WEIGHT*force_scale),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawVectorVert(Color.red, "Board Weight", x[0], y[0], y[1]);

          // ws rig weight
          to_screen_x_y(new Point3D((mast_xpos-WS_MASTBASE_MAST_LE),0,strut.span+2*BOARD_THICKNESS),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D((mast_xpos-WS_MASTBASE_MAST_LE),0,strut.span+2*BOARD_THICKNESS-(rider_weight*mast_foot_pressure_k+RIG_WEIGHT)*force_scale),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawVectorVert(Color.red, "MFP", x[0], y[0], y[1]);



          if (craft_type == WINDFOIL)
            drawVectorHoriz(Color.red, "Sail FWD Drive", 
                            screen_off_y+toInt(scaley*( rider_drive_center_y + 1)), // ~1 is distance to sail COE above hands/boom
                            screen_off_x+toInt(scalex*(mast_xpos)), 
                            screen_off_x+toInt(scalex*(mast_xpos - total_drag()*force_scale)));
          else
            drawVectorHoriz(Color.red, "Kite FWD Drive", 
                            screen_off_y+toInt(scaley*( rider_drive_center_y)), // 
                            screen_off_x+toInt(scalex*(rider_rot_center_x - 0.1)), 
                            screen_off_x+toInt(scalex*(rider_rot_center_x - 0.1 - total_drag()*force_scale)));

          drag_x = offx + strut.xpos + 0.5*strut.chord;
          // why -offy/2 ? because offy is negative
          to_screen_x_y(new Point3D(drag_x,0,-offy/2),x,y,0,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          to_screen_x_y(new Point3D(drag_x+(strut.drag)*force_scale,0,-offy/2),x,y,1,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawVectorHoriz(Color.red, "Mast Drag", y[0], x[0], x[1]);

        } else if (viewflg == VIEW_3D_MESH) {
          off1Gg.setColor(color_very_dark);
          off1Gg.fillRect(0,0,panel_width, panel_height);

          int screen_off_x = panel_width/2, screen_off_y = panel_height/2; // is at waterline

          // 150 pixels is 1.5m
          scalex = 4*zoom_slider_pos_y; scaley = -scalex;
          offx = -strut.xpos - strut.chord/2;
          offy = -strut.span/3;
          
          drawPart3D(strut,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawPart3D( fuse,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawPart3D( wing,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          drawPart3D( stab,offx,scalex,offy,scaley,screen_off_x,screen_off_y);
          
        } else {

          off1Gg.setColor(color_very_dark);
          off1Gg.fillRect(0,0,panel_width, panel_height);

          col_bg = color_very_dark;
          if (velocity > .01) {
            /* plot airfoil flowfield */
            radvec = .5;
            for (j=STREAMLINES_SKIP_BOTTOM; j<=STREAMLINES_COUNT_HALF-1; ++j) {           /* streamlines lower half */
              for (i=POINTS_COUNT_HALF; i<= POINTS_COUNT-1; ++i) {
                x[0] = (int) (fact*xpl[j][i]) + xt;
                y[0] = (int) (fact*(-ypl[j][i])) + yt;
                slope = (ypl[j][i+1]-ypl[j][i])/(xpl[j][i+1]-xpl[j][i]);
                xvec = xpl[j][i] + radvec / Math.sqrt(1.0 + slope*slope);
                yvec = ypl[j][i] + slope * (xvec - xpl[j][i]);
                x[1] = (int) (fact*xvec) + xt;
                y[1] = (int) (fact*(-yvec)) + yt;
                if (displ == DISPLAY_STREAMLINES) {                   /* MODS  21 JUL 99 */
                  off1Gg.setColor(Color.yellow);
                  x[1] = (int) (fact*xpl[j][i+1]) + xt;
                  y[1] = (int) (fact*(-ypl[j][i+1])) + yt;
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                } else if (displ == DISPLAY_DIRECTION  && (i/3*3 == i) ) {
                  off1Gg.setColor(col);
                  for (n=1; n <= 4; ++n) {
                    if (i == 6 + (n-1)*9) off1Gg.setColor(Color.yellow);
                  }
                  if (i/9*9 == i) off1Gg.setColor(Color.white);
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                } else if (displ == DISPLAY_ANIMATION  && ((i-animation_count)/3*3 == (i-animation_count)) ) {
                  if (color_flip) { 
                    if ((i-animation_count)/6*6 == (i-animation_count))
                      off1Gg.setColor(Color.white);
                    else 
                      off1Gg.setColor(col);
                  } else { 
                    if ((i-animation_count)/6*6 == (i-animation_count))
                      off1Gg.setColor(col);
                    else 
                      off1Gg.setColor(Color.white);
                  }
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                }
              }
            }
 
            off1Gg.setColor(Color.white); /* stagnation */
            x[1] = (int) (fact*xpl[STREAMLINES_COUNT_HALF][1]) + xt;
            y[1] = (int) (fact*(-ypl[STREAMLINES_COUNT_HALF][1])) + yt;
            for (i=2; i<= POINTS_COUNT_HALF-1; ++i) {
              x[0] = x[1];
              y[0] = y[1];
              x[1] = (int) (fact*xpl[STREAMLINES_COUNT_HALF][i]) + xt;
              y[1] = (int) (fact*(-ypl[STREAMLINES_COUNT_HALF][i])) + yt;
              if (displ <= 2) {             /* MODS  21 JUL 99 */
                off1Gg.drawLine(x[0],y[0],x[1],y[1]);
              }
            }
            x[1] = (int) (fact*xpl[STREAMLINES_COUNT_HALF][POINTS_COUNT_HALF+1]) + xt;
            y[1] = (int) (fact*(-ypl[STREAMLINES_COUNT_HALF][POINTS_COUNT_HALF+1])) + yt;
            for (i=POINTS_COUNT_HALF+2; i<= POINTS_COUNT; ++i) {
              x[0] = x[1];
              y[0] = y[1];
              x[1] = (int) (fact*xpl[STREAMLINES_COUNT_HALF][i]) + xt;
              y[1] = (int) (fact*(-ypl[STREAMLINES_COUNT_HALF][i])) + yt;
              if (displ <= 2) {                         /* MODS  21 JUL 99 */
                off1Gg.drawLine(x[0],y[0],x[1],y[1]);
              }
            }
            /*  probe location */
            if (pboflag > 0 && pypl <= 0.0) {
              off1Gg.setColor(Color.magenta);
              off1Gg.fillOval((int) (fact*pxpl) + xt,
                              (int) (fact*(-pypl)) + yt - 2,5,5);
              off1Gg.setColor(Color.white);
              x[0] = (int) (fact*(pxpl + .1)) +xt;
              y[0] = (int) (fact*(-pypl)) + yt;
              x[1] = (int) (fact*(pxpl + .5)) +xt;
              y[1] = (int) (fact*(-pypl)) + yt;
              x[2] = (int) (fact*(pxpl + .5)) +xt;
              y[2] = (int) (fact*(-pypl +50.)) +yt;
              off1Gg.drawLine(x[0],y[0],x[1],y[1]);
              off1Gg.drawLine(x[1],y[1],x[2],y[2]);
              if (pboflag == 3) {    /* smoke trail  MODS  21 JUL 99 */
                off1Gg.setColor(Color.green);
                for (i=1; i<= POINTS_COUNT-1; ++i) {
                  x[0] = (int) (fact*xpl[19][i]) + xt;
                  y[0] = (int) (fact*(-ypl[19][i])) + yt;
                  slope = (ypl[19][i+1]-ypl[19][i])/(xpl[19][i+1]-xpl[19][i]);
                  xvec = xpl[19][i] + radvec / Math.sqrt(1.0 + slope*slope);
                  yvec = ypl[19][i] + slope * (xvec - xpl[19][i]);
                  x[1] = (int) (fact*xvec) + xt;
                  y[1] = (int) (fact*(-yvec)) + yt;
                  if ((i-animation_count)/3*3 == (i-animation_count) ) {
                    off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                  }
                }
              }
            }
 
            //  wing surface
            if (viewflg == VIEW_3D_MESH) {           // 3d geom, mesh for now
              off1Gg.setColor(color_very_dark);
              x[1] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) + xt1;
              y[1] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF])) + yt1;
              x[2] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) + xt2;
              y[2] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF])) + yt2;
              for (i=1; i<= POINTS_COUNT_HALF-1; ++i) {
                x[0] = x[1];
                y[0] = y[1];
                x[1] = (int) (fact*(xpl[0][POINTS_COUNT_HALF-i])) + xt1;
                y[1] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF-i])) + yt1;
                x[3] = x[2];
                y[3] = y[2];
                x[2] = (int) (fact*(xpl[0][POINTS_COUNT_HALF-i])) + xt2;
                y[2] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF-i])) + yt2;
                off1Gg.fillPolygon(x,y,4);
              }
            }

            for (j=STREAMLINES_COUNT_HALF+1; 
                 j<=STREAMLINES_COUNT; 
                 ++j) {          /* upper half */
              for (i= POINTS_COUNT_HALF; i<= POINTS_COUNT-1; ++i) {
                x[0] = (int) (fact*xpl[j][i]) + xt;
                y[0] = (int) (fact*(-ypl[j][i])) + yt;
                slope = (ypl[j][i+1]-ypl[j][i])/(xpl[j][i+1]-xpl[j][i]);
                xvec = xpl[j][i] + radvec / Math.sqrt(1.0 + slope*slope);
                yvec = ypl[j][i] + slope * (xvec - xpl[j][i]);
                x[1] = (int) (fact*xvec) + xt;
                y[1] = (int) (fact*(-yvec)) + yt;
                if (displ == DISPLAY_STREAMLINES) {                     /* MODS  21 JUL 99 */
                  off1Gg.setColor(col);
                  x[1] = (int) (fact*xpl[j][i+1]) + xt;
                  y[1] = (int) (fact*(-ypl[j][i+1])) + yt;
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                } else if (displ == DISPLAY_DIRECTION && (i/3*3 == i) ) {
                  off1Gg.setColor(col);   /* MODS  27 JUL 99 */
                  for (n=1; n <= 4; ++n) {
                    if (i == 6 + (n-1)*9) off1Gg.setColor(Color.yellow);
                  }
                  if (i/9*9 == i) off1Gg.setColor(Color.white);
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                } else if (displ == DISPLAY_ANIMATION && ((i-animation_count)/3*3 == (i-animation_count)) ) {
                  if (color_flip) {
                    if ((i-animation_count)/6*6 == (i-animation_count))
                      off1Gg.setColor(Color.white);
                    else 
                      off1Gg.setColor(col);
                  } else {
                    if ((i-animation_count)/6*6 == (i-animation_count))
                      off1Gg.setColor(col);
                    else 
                      off1Gg.setColor(Color.white);
                  }
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                }
              }
            }
            /*  probe location */
            if (pboflag > 0 && pypl > 0.0) {
              off1Gg.setColor(Color.magenta);
              off1Gg.fillOval((int) (fact*pxpl) + xt,
                              (int) (fact*(-pypl)) + yt - 2,5,5);
              off1Gg.setColor(Color.white);
              x[0] = (int) (fact*(pxpl + .1)) +xt;
              y[0] = (int) (fact*(-pypl)) + yt;
              x[1] = (int) (fact*(pxpl + .5)) +xt;
              y[1] = (int) (fact*(-pypl)) + yt;
              x[2] = (int) (fact*(pxpl + .5)) +xt;
              y[2] = (int) (fact*(-pypl -50.)) +yt;
              off1Gg.drawLine(x[0],y[0],x[1],y[1]);
              off1Gg.drawLine(x[1],y[1],x[2],y[2]);
              if (pboflag == 3) {    /* smoke trail  MODS  21 JUL 99 */
                off1Gg.setColor(Color.green);
                for (i=1; i<= POINTS_COUNT-1; ++i) {
                  x[0] = (int) (fact*xpl[19][i]) + xt;
                  y[0] = (int) (fact*(-ypl[19][i])) + yt;
                  slope = (ypl[19][i+1]-ypl[19][i])/(xpl[19][i+1]-xpl[19][i]);
                  xvec = xpl[19][i] + radvec / Math.sqrt(1.0 + slope*slope);
                  yvec = ypl[19][i] + slope * (xvec - xpl[19][i]);
                  x[1] = (int) (fact*xvec) + xt;
                  y[1] = (int) (fact*(-yvec)) + yt;
                  if ((i-animation_count)/3*3 == (i-animation_count) ) {
                    off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                  }
                }
              }
            }
          }
 
          if (viewflg == VIEW_EDGE) {
            // draw the airfoil geometry
            if (f.geometry != null) { // use xm, ym directly
              off1Gg.setColor(Color.yellow);
              x[1] = (int) (fact*(xm[0][0])) + xt;
              y[1] = (int) (fact*(-ym[0][0])) + yt;
              for (i=1; i< 61; ++i) {
                x[0] = x[1];
                y[0] = y[1];
                x[1] = (int) (fact*(xm[0][i])) + xt;
                y[1] = (int) (fact*(-ym[0][i])) + yt;
                off1Gg.drawLine(x[0],y[0],x[1],y[1]);
              }
              if (f.camber_line != null) { // add camber line 
                // scale and rotate on fly
                double rad_aoa = Math.toRadians(effective_aoa());
                off1Gg.setColor(Color.red);
                Point2D[] cl = f.camber_line;
                double dx = fact*(3.5 * (cl[0].x - 0.5));
                double dy = fact*(-3.5 * cl[0].y);
                double rot_dx = dx*Math.cos(rad_aoa) - dy*Math.sin(rad_aoa);
                double rot_dy = dx*Math.sin(rad_aoa) + dy*Math.cos(rad_aoa);
                int prev_x =  (int)(rot_dx) + xt; 
                // System.out.println("-- prev_x: " + prev_x);
                int prev_y =  (int)(rot_dy) + yt; 
                // System.out.println("-- prev_y: " + prev_y);
                for (n=1; n < 30; ++n) {
                  dx = fact*(3.5 * (cl[n].x - 0.5));
                  dy = fact*(-3.5 * cl[n].y);
                  rot_dx = dx*Math.cos(rad_aoa) - dy*Math.sin(rad_aoa);
                  rot_dy = dx*Math.sin(rad_aoa) + dy*Math.cos(rad_aoa);
                  int curr_x = (int)(rot_dx) + xt; 
                  // System.out.println("-- curr_x: " + curr_x);
                  int curr_y = (int)(rot_dy) + yt; 
                  // System.out.println("-- curr_y: " + curr_y);
                  off1Gg.drawLine(prev_x, prev_y, curr_x, curr_y);
                  prev_x = curr_x;
                  prev_y = curr_y;
                }
              }
            } else {
              x[1] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) + xt;
              y[1] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF])) + yt;
              x[2] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) + xt;
              y[2] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF])) + yt;
              //camb_x[0] = 0; // (x[1] + x[2]) / 2;
              //camb_y[0] = 0; // (y[1] + y[2]) / 2;
              for (i=1; i<= POINTS_COUNT_HALF-1; ++i) {
                x[0] = x[1];
                y[0] = y[1];
                x[1] = (int) (fact*(xpl[0][POINTS_COUNT_HALF-i])) + xt;
                y[1] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF-i])) + yt;
                x[3] = x[2];
                y[3] = y[2];
                x[2] = (int) (fact*(xpl[0][POINTS_COUNT_HALF+i])) + xt;
                y[2] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF+i])) + yt;
                camb_x[i] = (x[1] + x[2]) / 2;
                camb_y[i] = (y[1] + y[2]) / 2;
                if (f == FOIL_FLAT_PLATE) {
                  off1Gg.setColor(Color.yellow);
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                }
                else {
                  off1Gg.setColor(Color.white);
                  //off1Gg.fillPolygon(x,y,4);
                  //off1Gg.drawPolygon(x,y,4);
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                  off1Gg.drawLine(x[2],y[2],x[3],y[3]);
                  // off1Gg.setColor(Color.red);
                  // off1Gg.drawLine(camb_x[i-1], camb_y[i-1], camb_x[i], camb_y[i]);
                }
              }

              // always draw camber line
              inmax = 1;
              if (!foil_is_cylinder_or_ball(f)) {
                off1Gg.setColor(Color.red);
                { 
                  for (n=1; n <= POINTS_COUNT; ++n) {
                    if (xpl[0][n] > xpl[0][inmax]) inmax = n;
                  }
                  x[1] = (int) (fact*(xpl[0][inmax] -
                                      4.0*Math.cos(convdr*effective_aoa())))+xt;
                  y[1] = (int) (fact*(-ypl[0][inmax] -
                                      4.0*Math.sin(convdr*effective_aoa())))+yt;
                  off1Gg.drawLine(x[1],y[1],camb_x[5],camb_y[5]);
                  for (i=7; i<= POINTS_COUNT_HALF-6; i = i+2) {
                    off1Gg.drawLine(camb_x[i],camb_y[i],camb_x[i+1],camb_y[i+1]);
                  }
                }
              }

              // put some info on the geometry
              if (displ == DISPLAY_GEOMETRY) {
                if (!foil_is_cylinder_or_ball(f)) {
                  off1Gg.setColor(Color.green);
                  x[0] = (int) (fact*(xpl[0][inmax])) + xt;
                  y[0] = (int) (fact*(-ypl[0][inmax])) + yt;
                  off1Gg.drawLine(x[0],y[0],x[0]-250,y[0]);
                  off1Gg.drawString("Reference",30,y[0]+10);
                  off1Gg.drawString("Angle",x[0]+20,y[0]);
      
                  off1Gg.setColor(Color.cyan);
                  x[1] = (int) (fact*(xpl[0][inmax] -
                                      4.0*Math.cos(convdr*effective_aoa())))+xt;
                  y[1] = (int) (fact*(-ypl[0][inmax] -
                                      4.0*Math.sin(convdr*effective_aoa())))+yt;
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                  off1Gg.drawString("Chord Line",x[0]+20,y[0]+20);
   
                  off1Gg.setColor(Color.red);
                  off1Gg.drawString("Mean Camber Line",x[0]-70,y[1]-10);
                } else {
                  off1Gg.setColor(Color.red);
                  x[0] = (int) (fact*(xpl[0][1])) + xt;
                  y[0] = (int) (fact*(-ypl[0][1])) + yt;
                  x[1] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) +xt;
                  y[1] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF])) + yt;
                  off1Gg.drawLine(x[0],y[0],x[1],y[1]);
                  off1Gg.drawString("Diameter",x[0]+20,y[0]+20);
                }
   
                off1Gg.setColor(Color.green);
                off1Gg.drawString("Flow",30,145);
                off1Gg.drawLine(30,152,60,152);
                x[0] = 60;  x[1] = 60; x[2] = 70;
                y[0] = 157;  y[1] = 147; y[2] = 152;
                off1Gg.fillPolygon(x,y,3);
              }
              //  spin the cylinder and ball
              if (foil_is_cylinder_or_ball(f)) {
                x[0] = (int) (fact* (.5*(xpl[0][1] + xpl[0][POINTS_COUNT_HALF]) +
                                     solver.rval * Math.cos(convdr*(ball_spin_angle + 180.)))) + xt;
                y[0] = (int) (fact* (-ypl[0][1] +
                                     solver.rval * Math.sin(convdr*(ball_spin_angle + 180.)))) + yt;
                x[1] = (int) (fact* (.5*(xpl[0][1] + xpl[0][POINTS_COUNT_HALF]) +
                                     solver.rval * Math.cos(convdr*ball_spin_angle))) + xt;
                y[1] = (int) (fact* (-ypl[0][1] +
                                     solver.rval * Math.sin(convdr*ball_spin_angle))) + yt;
                off1Gg.setColor(Color.red);
                off1Gg.drawLine(x[0],y[0],x[1],y[1]);
              } 
            }
          }
          if (viewflg == 1000 ) { // this is old Side-3d view
            //   front foil
            off1Gg.setColor(Color.white);
            x[1] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) + xt2;
            y[1] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF])) + yt2;
            x[2] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) + xt2;
            y[2] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF])) + yt2;
            for (i=1; i<= POINTS_COUNT_HALF-1; ++i) {
              x[0] = x[1];
              y[0] = y[1];
              x[1] = (int) (fact*(xpl[0][POINTS_COUNT_HALF-i])) + xt2;
              y[1] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF-i])) + yt2;
              x[3] = x[2];
              y[3] = y[2];
              x[2] = (int) (fact*(xpl[0][POINTS_COUNT_HALF+i])) + xt2;
              y[2] = (int) (fact*(-ypl[0][POINTS_COUNT_HALF+i])) + yt2;
              camb_x[i] = (x[1] + x[2]) / 2;
              camb_y[i] = (y[1] + y[2]) / 2;
              off1Gg.fillPolygon(x,y,4);
            }
            // put some info on the geometry
            if (displ == DISPLAY_GEOMETRY) {
              off1Gg.setColor(Color.green);
              x[1] = (int) (fact*(xpl[0][1])) + xt1 + 20;
              y[1] = (int) (fact*(-ypl[0][1])) + yt1;
              x[2] = (int) (fact*(xpl[0][1])) + xt2 + 20;
              y[2] = (int) (fact*(-ypl[0][1])) + yt2;
              off1Gg.drawLine(x[1],y[1],x[2],y[2]);
              off1Gg.drawString("Span",x[2]+10,y[2]+10);

              x[1] = (int) (fact*(xpl[0][1])) + xt2;
              y[1] = (int) (fact*(-ypl[0][1])) + yt2 + 15;
              x[2] = (int) (fact*(xpl[0][POINTS_COUNT_HALF])) + xt2;
              y[2] = y[1];
              off1Gg.drawLine(x[1],y[1],x[2],y[2]);
              if (!foil_is_cylinder_or_ball(f)) 
                off1Gg.drawString("Chord",x[2]+10,y[2]+15);
              else off1Gg.drawString("Diameter",x[2]+10,y[2]+15);

              off1Gg.drawString("Flow",40,75);
              off1Gg.drawLine(30,82,60,82);
              x[0] = 60;  x[1] = 60; x[2] = 70;
              y[0] = 87;  y[1] = 77; y[2] = 82;
              off1Gg.fillPolygon(x,y,3);
            }
            //  spin the cylinder and ball
            if (foil_is_cylinder_or_ball(f)) {
              x[0] = (int) (fact* (.5*(xpl[0][1] + xpl[0][POINTS_COUNT_HALF]) +
                                      solver.rval * Math.cos(convdr*(ball_spin_angle + 180.)))) + xt2;
              y[0] = (int) (fact* (-ypl[0][1] +
                                      solver.rval * Math.sin(convdr*(ball_spin_angle + 180.)))) + yt2;
              x[1] = (int) (fact* (.5*(xpl[0][1] + xpl[0][POINTS_COUNT_HALF]) +
                                      solver.rval * Math.cos(convdr*ball_spin_angle))) + xt2;
              y[1] = (int) (fact* (-ypl[0][1] +
                                      solver.rval * Math.sin(convdr*ball_spin_angle))) + yt2;
              off1Gg.setColor(Color.red);
              off1Gg.drawLine(x[0],y[0],x[1],y[1]);
            }
          }
        }

        // JLabels
        // off1Gg.setColor(col_bg);
        // off1Gg.fillRect(0,0,350,30);
        off1Gg.setColor(Color.white);
        off1Gg.drawString("View:",35,10);
        if (viewflg == VIEW_EDGE) off1Gg.setColor(Color.yellow);
        else off1Gg.setColor(Color.cyan);
        off1Gg.drawString("Edge",95,10);
        if (viewflg == VIEW_FORCES) off1Gg.setColor(Color.yellow);
        else off1Gg.setColor(Color.cyan);
        off1Gg.drawString("Forces",130,10);

        if (viewflg == VIEW_3D_MESH) off1Gg.setColor(Color.yellow);
        else off1Gg.setColor(Color.cyan);
        off1Gg.drawString("3D Mesh",180,10);


        off1Gg.setColor(Color.white);
        off1Gg.drawString("Display:",35,25);

        if (viewflg == VIEW_FORCES) {
          off1Gg.setColor(force_labels ? Color.yellow : Color.cyan);
          off1Gg.drawString("Force Labels",85,25);
        } else if (viewflg == VIEW_3D_MESH) {
          off1Gg.setColor(perspective ? Color.cyan :  Color.yellow);
          off1Gg.drawString("Ortographic",85,25);
          off1Gg.setColor(perspective ? Color.yellow : Color.cyan);
          off1Gg.drawString("Perspective",160,25);
        } else {

          off1Gg.setColor(Color.red);
          off1Gg.drawString("Find",240,10);

          if (displ == DISPLAY_STREAMLINES) 
            off1Gg.setColor(Color.yellow);
          else 
            off1Gg.setColor(Color.cyan);
          off1Gg.drawString("Streamlines",85,25);

          if (displ == 1) off1Gg.setColor(Color.yellow);
          else off1Gg.setColor(Color.cyan);
          off1Gg.drawString("Moving",160,25);
          if (displ == 2) off1Gg.setColor(Color.yellow);
          else off1Gg.setColor(Color.cyan);
          off1Gg.drawString("Frozen",210,25);
          if (displ == 3) off1Gg.setColor(Color.yellow);
          else off1Gg.setColor(Color.cyan);
          off1Gg.drawString("Geometry",260,25);
        }
        // controls 

        //off1Gg.setColor(col_bg);
        //off1Gg.fillRect(0,30,30,150);
        // off1Gg.setColor(zoom_widget_active ? Color.white : Color.green);
        // off1Gg.drawString("Zoom",2,182);
        // off1Gg.drawLine(15,30,15,165);
        // //frame
        // // not nowoff1Gg.drawRect(3,26,24,144);
        // off1Gg.fillRect(5,zoom_slider_pos_y-3,20,6);
        drawSliderWidget("Zoom", 0, zoom_slider_pos_y, zoom_widget_active);

        Font currentFont = off1Gg.getFont();
        if (viewflg == VIEW_FORCES) {
          //off1Gg.setColor(force_scale_widget_active ? Color.white : Color.green);
          //off1Gg.drawString("Forces",30+2,182);
          //off1Gg.drawLine(30+15,30,30+15,165);
          //// frame 
          //// not now off1Gg.drawRect(30+3,26,24,144);
          //off1Gg.fillRect(30+5,force_scale_slider-3,20,6);
          drawSliderWidget("Forces", 30, force_scale_slider, force_scale_widget_active);

          off1Gg.setFont(largeFont);
          off1Gg.setColor(Color.yellow);
          off1Gg.drawString(t_foil_name, 20, panel_height-24);
          off1Gg.setFont(currentFont);
        // } else if (viewflg == VIEW_3D_MESH) {
        //   drawSliderWidget("   X", 30, mesh_x_angle_slider, mesh_x_angle_widget_active);
        //   drawSliderWidget("   Y", 60, mesh_z_angle_slider, mesh_z_angle_widget_active);
        } else {
          // display model and part in bold
          off1Gg.setFont(largeFont);
          off1Gg.setColor(Color.yellow);
          off1Gg.drawString(t_foil_name, 20, panel_height-24);
          if (viewflg != VIEW_3D_MESH)
            off1Gg.drawString("Part: " + part_button.getLabel(), 300, 30);
          off1Gg.setFont(currentFont);
        }
        off1Gg.drawString("V: " + speed_kts_mph_kmh_ms_info, 20, panel_height-4);

        g.drawImage(offImg1,0,0,this);   
      } // Viewer.paint ()
    } // end Viewer
 

    class Plot extends Canvas implements Runnable {
      FoilBoard app;
      Thread run2;

      int axis_y_label_width, axis_x_label_width, plot_trace_count;

      double begx,endx,begy,endy;
      String labx,labxu,laby,labyu;
      double[][] plotx = new double[5][50+1]; // 1 st x,y  oten used to store a current red "dot"
      double[][] ploty = new double[5][50+1];
      Color[] plot_colors = {Color.white, Color.magenta, Color.green};

      // state shared between loadPlot and paint
      int lines = 1;
      int ntikx,ntiky,npt, start_pt = 1;


      Point locp,ancp;

      Solver plot_solver = new Solver();

      Plot (FoilBoard target) { 
        setBackground(Color.blue);
        run2 = null;
      }
   
      public boolean mouseUp(Event evt, int x, int y) {
        handleButton(x,y);
        return true;                                        
      }

      int rescale_bt_y;
      boolean should_rescale_p = false;

      public void handleButton (int x, int y) {
        if (y >= rescale_bt_y) { 
          if (x >= 4 && x <= 68) {   // cliked on button rescale
            should_rescale_p = !should_rescale_p;
            out.plot.loadPlot();
          }
        }
        out.plot.repaint();
      }

      public void start() {
        if (run2 == null) {
          run2 = new Thread(this);
          run2.start();
        }
      }

      //      public void run() {
      //        int timer;
      // 
      //        timer = 100;
      //        while (true) {
      //          try { Thread.sleep(timer); }
      //          catch (InterruptedException e) {}
      //          out.plot.repaint();
      //        }
      //      }
      
      /**
       * @j2sNative
       * 
       * this.repaint();
       * var self = this; setTimeout(function() {self.run();}, 100);
       */
      public void run() {
        int timer;
 
        timer = 100;
        while (true) {
          try { Thread.sleep(timer); }
          catch (InterruptedException e) {}
          out.plot.repaint();
        }
      }

      // Plot.loadPlot
      public void loadPlot () {
        // new Exception("Plot.loadPlot").printStackTrace(System.out);
        double rad,ang,xc,yc,lftref,clref,drgref,cdref;
        double del,spd,awng,ppl,tpl,hpl,angl;
        int index,ic;
        double aoa_absolute = effective_aoa();
        double alfd = aoa_absolute;

        Foil f = current_part.foil;

        clref =  getCl_plot(current_part.camber/25,current_part.thickness/25,aoa_absolute);
        if (Math.abs(clref) <= .001) clref = .001;    /* protection */

        lftref = clref * q0_SI * current_part.area;
        
        //   attempt to fix symmetry problem
        if (current_part.camber < 0.0) alfd = - aoa_absolute;
        //
        cdref = solver.get_Cd(clref, alfd, current_part.thickness, current_part.camber);

        drgref = cdref * q0_SI * current_part.area;

        if (current_part == strut) {
          // typically only 30% sits in water
          drgref *= (1-alt/100);
        }
   
        // load up the view image
        for (ic = 0; ic <= STREAMLINES_COUNT; ++ ic) {
          for (index = 0; index <= POINTS_COUNT; ++ index) {
            if (!foil_is_cylinder_or_ball(f)) {
              xpl[ic][index] = xm[ic][index];
              ypl[ic][index] = ym[ic][index];
            } else {
              xpl[ic][index] = xg[ic][index];
              ypl[ic][index] = yg[ic][index];
            }
          }
        }

        // probe
        for (index = 0; index <= POINTS_COUNT; ++ index) {
          if (!foil_is_cylinder_or_ball(f)) {
            xpl[19][index] = xm[19][index];
            ypl[19][index] = ym[19][index];
            pxpl = pxm;
            pypl = pym;
          } else {
            xpl[19][index] = xg[19][index];
            ypl[19][index] = yg[19][index];
            pxpl = pxg;
            pypl = pyg;
          }
        }
              
        //  load up surface plots
        switch (plot_type) {
        case PLOT_TYPE_PRESSURE: { // pressure variation 
          npt = POINTS_COUNT_HALF;
          plot_trace_count = 3;
          axis_y_label_width = axis_x_label_width = 2;
          for (index = 1; index <= npt; ++ index) {
            if (!foil_is_cylinder_or_ball(f)) {
              plotx[0][index] =100.*(xpl[0][POINTS_COUNT_HALF-index + 1]/4.0 + .5);
              plotx[1][index] =100.*(xpl[0][POINTS_COUNT_HALF+index - 1]/4.0 + .5);
              plotx[2][index] =100.*(xpl[0][POINTS_COUNT_HALF+index - 1]/4.0 + .5);
            } else {
              plotx[0][index]=100.*(xpl[0][POINTS_COUNT_HALF-index+1]/(2.0*radius/lconv)+.5);
              plotx[1][index]=100.*(xpl[0][POINTS_COUNT_HALF+index-1]/(2.0*radius/lconv)+.5);
              plotx[2][index]=100.*(xpl[0][POINTS_COUNT_HALF+index-1]/(2.0*radius/lconv)+.5);
            }
            ploty[0][index] = plp[POINTS_COUNT_HALF-index + 1];
            ploty[1][index] = plp[POINTS_COUNT_HALF+index - 1];
            ploty[2][index] = ps0/2116. * pconv;
            // **** Impose pstatic on surface plot for stalled foil
            if (stall_model_type != STALL_MODEL_IDEAL_FLOW && index > 7) {
              double apos = stall_model_type == STALL_MODEL_DFLT ? +10 : stall_model_apos;
              double aneg = stall_model_type == STALL_MODEL_DFLT ? -10 : stall_model_aneg;
              if (aoa_absolute > apos) ploty[0][index] = ploty[2][index];
              if (aoa_absolute < aneg) ploty[1][index] = ploty[2][index];
            }
            // *******
          }
          begx = 0.0;
          endx = 100.;
          ntikx = 5;
          ntiky = 5;
          //       endy=1.02 * ps0/2116. * pconv;
          //       begy=.95 * ps0/2116. * pconv;
          laby = String.valueOf("Press");
          if (lunits == IMPERIAL) labyu = String.valueOf("psi");
          if (lunits == 1) labyu = String.valueOf("k-Pa");
          labx = String.valueOf(" X ");
          if (!foil_is_cylinder_or_ball(f)) labxu = String.valueOf("% chord");
          else labxu = String.valueOf("% diameter");
          break;
        }
        case PLOT_TYPE_VELOCITY: {   // velocity variation
          npt = POINTS_COUNT_HALF;
          plot_trace_count = 3;
          axis_y_label_width = 2;
          axis_x_label_width = 1;
          for (index = 1; index <= npt; ++ index) {
            if (!foil_is_cylinder_or_ball(f)) {
              plotx[0][index] = 100.*(xpl[0][POINTS_COUNT_HALF-index+1]/4.0+.5);
              plotx[1][index] = 100.*(xpl[0][POINTS_COUNT_HALF+index-1]/4.0+.5);
              plotx[2][index] = 100.*(xpl[0][POINTS_COUNT_HALF-index+1]/4.0+.5);
            } else {
              plotx[0][index]=100.*(xpl[0][POINTS_COUNT_HALF-index+1]/(2.0*radius/lconv)+.5);
              plotx[1][index]=100.*(xpl[0][POINTS_COUNT_HALF+index-1]/(2.0*radius/lconv)+.5);
              plotx[2][index]=100.*(xpl[0][POINTS_COUNT_HALF+index-1]/(2.0*radius/lconv)+.5);
            }
            ploty[0][index] = plv[POINTS_COUNT_HALF-index+1];
            ploty[1][index] = plv[POINTS_COUNT_HALF+index-1];
            ploty[2][index] = velocity;
            // **** Impose free stream vel on surface plot for stalled foil
            if (stall_model_type != STALL_MODEL_IDEAL_FLOW && index > 7) {
              double apos = stall_model_type == STALL_MODEL_DFLT ? +10 : stall_model_apos;
              double aneg = stall_model_type == STALL_MODEL_DFLT ? -10 : stall_model_aneg;
              if (aoa_absolute > apos) ploty[0][index] = ploty[2][index];
              if (aoa_absolute < aneg) ploty[1][index] = ploty[2][index];
            }
            // *******
          }
          begx = 0.0;
          endx = 100.;
          ntikx = 5;
          ntiky = 6;
          //      begy = 0.0;
          //      endy = 500.;
          laby = String.valueOf("Vel");
          if (lunits == IMPERIAL) labyu = String.valueOf("mph");
          if (lunits == 1) labyu = String.valueOf("kmh");
          labx = String.valueOf(" X ");
          if (!foil_is_cylinder_or_ball(f)) labxu = String.valueOf("% chord");
          else labxu = String.valueOf("% diameter");

          //  load up performance plots
          break;
        }
        case PLOT_TYPE_ANGLE: { // lift/cl/drag/cd versus angle
          npt = 21;
          plot_trace_count = (plot_y_val == PLOT_OUT_DRAG) ? 3 : 1;
          axis_x_label_width = 2;  axis_y_label_width = 3;
          begx=-20.0; endx=20.0; ntikx=5;
          labx = String.valueOf("Angle");
          labxu = String.valueOf("degrees");
          del = 40.0 / (npt-1);
          for (ic=1; ic <=npt; ++ic) {
            angl = -20.0 + (ic-1)*del;
            double clpl = getCl_plot(current_part.camber/25,current_part.thickness/25,angl);
            alfd = angl;
            double thkd = current_part.thickness;
            double camd = current_part.camber;

            //   attempt to fix symmetry problem
            if (camd < 0.0) alfd = - angl;
            //
            double cdpl = solver.get_Cd(clpl, alfd, thkd, camd);

            if ( plot_y_val == PLOT_OUT_LIFT || plot_y_val == PLOT_OUT_CL) {
              plotx[0][ic] = angl;
              if (plot_y_val == PLOT_OUT_LIFT) 
                ploty[0][ic] = lftref * clpl/clref;
              else 
                ploty[0][ic] = clpl;
            }
            else {
              plotx[0][ic] = angl;
              if (plot_y_val == PLOT_OUT_DRAG)
                ploty[0][ic] = drgref * cdpl/cdref;
              else 
                ploty[0][ic] = cdpl;
            }
          }
          ntiky = 5;
          plotx[1][0] = aoa_absolute;
          switch (plot_y_val) {
          case PLOT_OUT_LIFT:
            laby = String.valueOf("Lift");
            if (lunits == IMPERIAL) labyu = String.valueOf("lbs");
            if (lunits == 1) labyu = String.valueOf("N");
            ploty[1][0] = lftref;
            break;
          case PLOT_OUT_CL:
            laby = String.valueOf("Cl");
            labyu = String.valueOf("");
            ploty[1][0] = current_part.cl;
            break;
          case PLOT_OUT_DRAG:
            laby = String.valueOf("Drag");
            if (lunits == IMPERIAL) labyu = String.valueOf("lbs");
            if (lunits == 1) labyu = String.valueOf("N");
            ploty[1][0] = drgref;
            break;
          case PLOT_OUT_CD:
            laby = String.valueOf("Cd");
            labyu = String.valueOf("x 100 ");
            ploty[1][0] = 100.*current_part.cd;
            break;
          default:
          } 
      
          break;
        }
        case PLOT_TYPE_THICKNESS: { // lift/cl/drag/cd versus thickness
            npt = 20;
            plot_trace_count = 1;
            axis_x_label_width = 3;  axis_y_label_width = 3;
            begx=0.0; 
            endx=20.0; 
            ntikx=5;
            labx = String.valueOf("Thickness ");
            labxu = String.valueOf("% chord");
            del = 1.0 / (npt);
            for (ic=1; ic <=npt; ++ic) {
              double thkpl = .05 + (ic-1)*del;
              double clpl = getCl_plot(current_part.camber/25,thkpl,aoa_absolute);
              alfd = aoa_absolute;
              double thkd = thkpl*25.0;
              double camd = current_part.camber;
              //   attempt to fix symmetry problem
              if (camd < 0.0) alfd = - aoa_absolute;
              //
              double cdpl = solver.get_Cd(clpl, alfd, thkd, camd);

              if ( plot_y_val == PLOT_OUT_LIFT || plot_y_val == PLOT_OUT_CL) {
                plotx[0][ic] = thkpl*25.;
                if (plot_y_val == PLOT_OUT_LIFT)
                  ploty[0][ic] = lftref * clpl/clref;
                else 
                  ploty[0][ic] = 100.*clpl;
              }
              else {
                plotx[0][ic] = thkd;
                if (plot_y_val == PLOT_OUT_DRAG)
                  ploty[0][ic] = drgref * cdpl/cdref;
                else
                  ploty[0][ic] = 100.*cdpl;
              }
            }
            ntiky = 5;
            plotx[1][0] = current_part.thickness;
            switch (plot_y_val) {
            case PLOT_OUT_LIFT:
              laby = String.valueOf("Lift");
              labyu = current_display_force_unit_string();
              ploty[1][0] = lftref;
              break;
            case PLOT_OUT_CL:
              laby = String.valueOf("Cl");
              ploty[1][0] = current_part.cl;
              break;
            case PLOT_OUT_DRAG:
              laby = String.valueOf("Drag");
              labyu = current_display_force_unit_string();
              ploty[1][0] = drgref;
              ploty[0][npt]= ploty[0][npt-1]= ploty[0][npt-2]=ploty[0][npt-3]=ploty[0][npt-4];
              break;
            case PLOT_OUT_CD:
              laby = String.valueOf("Cd");
              ploty[1][0] = current_part.cd;
              ploty[0][npt] = ploty[0][npt-1] = ploty[0][npt-2]=ploty[0][npt-3]=ploty[0][npt-4];
              break;
            default:
            }
            break;
        }
        case PLOT_TYPE_CAMBER: { // lift/cl/drag/cd versus camber
            npt = 21;
            plot_trace_count = 1;
            axis_x_label_width = 4;  axis_y_label_width = 3;
            begx=-20.; 
            endx=20.; 
            ntikx=5;
            labx = String.valueOf("Camber ");
            labxu = String.valueOf("% chord");
            del = 2.0 / (npt-1);
            for (ic=1; ic <=npt; ++ic) {
              double campl = -1.0 + (ic-1)*del;
              double clpl = getCl_plot(campl,current_part.thickness/25,aoa_absolute);
              alfd = aoa_absolute;
              double thkd = current_part.thickness;
              double camd = campl * 25.0;
              //   attempt to fix symmetry problem
              if (camd < 0.0) alfd = - aoa_absolute;
              //
              double cdpl = solver.get_Cd(clpl, alfd, thkd, camd);

              if ( plot_y_val == PLOT_OUT_LIFT || plot_y_val == PLOT_OUT_CL) {
                plotx[0][ic] = campl*25.0;
                if (plot_y_val == PLOT_OUT_LIFT)
                  ploty[0][ic] = lftref * clpl/clref;
                else 
                  ploty[0][ic] = clpl;
              }
              else {
                plotx[0][ic] = camd;
                if (plot_y_val == PLOT_OUT_DRAG)
                  ploty[0][ic] = drgref * cdpl/cdref;
                else
                  ploty[0][ic] = cdpl;
              }
            }
            ntiky = 5;
            plotx[1][0] = current_part.camber;
            switch (plot_y_val) {
            case PLOT_OUT_LIFT:
              laby = String.valueOf("Lift");
              labyu = current_display_force_unit_string();
              ploty[1][0] = lftref;
              break;
            case PLOT_OUT_CL:
              laby = String.valueOf("Cl");
              ploty[1][0] = current_part.cl;
              break;
            case PLOT_OUT_DRAG:
              laby = String.valueOf("Drag");
              labyu = current_display_force_unit_string();
              ploty[1][0] = drgref;
              ploty[0][1] = ploty[0][2]= ploty[0][3];
              ploty[0][npt] = ploty[0][npt -1] = ploty[0][npt - 2];
              break;
            case PLOT_OUT_CD:
              laby = String.valueOf("Cd");
              ploty[1][0] = current_part.cd;
              ploty[0][1] = ploty[0][2]= ploty[0][3];
              ploty[0][npt] = ploty[0][npt -1] = ploty[0][npt-2];
              break;
            default:
            }
            break;
        }
        case PLOT_TYPE_CG_VS_SPEED: { // rider position versus speed
          on_cg_plotting = true; // for debugging only 

          ntiky = 8;
          laby = "";
          labyu = "";

          npt = 40;
          start_pt = 1;
          plot_trace_count = 4;
          axis_x_label_width = 5;  axis_y_label_width = 3;

          labx = String.valueOf("Speed ");
          // spd = Math.min(8, velocity);
          spd = 8;
          labxu = current_display_speed_unit_string();
          double beg_spd = 4+load/100; //ad hoc  old: min_takeoff_speed == 0 ? 15 : min_takeoff_speed; // velocity/2; // 10;
          double end_spd = 50;
          del = (npt == 1) ? 0 : (end_spd-beg_spd)/(npt-1);

          plotx[0][0] = plotx[1][0]  = plotx[2][0] = plotx[3][0] = speed_kmh_to_display_units(velocity);
          ploty[0][0] = con.cg_pos_board_level * 
            ((display_units == METRIC || display_units == METRIC_2) ? 100 : 39.3701);
          ploty[1][0] = (display_units == METRIC) 
              ? 0.10197*total_drag() : force_n_to_display_units(total_drag());
          ploty[2][0] = craft_pitch;
          ploty[3][0] = total_lift()/total_drag();

          boolean saved_flag = can_do_gui_updates;
          can_do_gui_updates = false;;
          double saved_velocity = velocity;
          double saved_craft_pitch = craft_pitch;

          for (spd = end_spd, ic=npt; ic >= 1; ic--, spd-=del) {
            velocity = spd;
            //? vpp.steady_flight_at_given_speed(5, craft_pitch);
            if (spd == end_spd)
              vpp.steady_flight_at_given_speed(2, 0);
            else
              vpp.steady_flight_at_given_speed(1, craft_pitch);
            // con.recomp_all_parts(); // just in case
            double total_drag = total_drag();


            double total_lift = total_lift();
            if (total_lift < load) { // halt
              start_pt = ic+1;
              break;
            }
              
            plotx[0][ic] = 
            plotx[1][ic] = 
            plotx[2][ic] = 
            plotx[3][ic] = speed_kmh_to_display_units(velocity);
            ploty[0][ic] = con.cg_pos_board_level * 
            ((display_units == METRIC || display_units == METRIC_2) ? 100 : 39.3701);
            ploty[1][ic] = (display_units == METRIC) 
              ? 0.10197*total_drag : force_n_to_display_units(total_drag);
            ploty[2][ic] = craft_pitch;
            ploty[3][ic] = total_lift/total_drag;
          }

          velocity = saved_velocity;
          vpp.steady_flight_at_given_speed(5, 0);
          can_do_gui_updates = saved_flag;
          // this is needed for imported foils so that outline is recomputed
          solver.compute_foil_geometry(effective_aoa());
          craft_pitch = saved_craft_pitch;

          on_cg_plotting = false;

          break;
        }

        case PLOT_TYPE_CURR_PART_VS_SPEED: { // various drag sources versus speed
          ntiky = 7;
          laby = current_display_force_unit_string();
          labyu = "";

          npt = 40;
          plot_trace_count = 5;
          axis_x_label_width = 5;  axis_y_label_width = 3;

          labx = String.valueOf("Speed");
          // spd = Math.min(8, velocity);
          spd = 8;
          labxu = current_display_speed_unit_string();
          double beg_spd = 5;
          double end_spd = 50;
          del = (npt == 1) ? 0 : (end_spd-beg_spd)/(npt-1);

          boolean saved_flag = can_do_gui_updates;
          can_do_gui_updates = false;;
          //con.recomp_all_parts(); // only make sure we sync cache with saved part props
          double saved_velocity = velocity;

          plotx[0][0] = plotx[1][0]  = plotx[2][0] = plotx[3][0] = plotx[4][0] = 
            speed_kmh_to_display_units(velocity);

          ploty[0][0] = Math.log10(force_n_to_display_units(current_part.drag)); // total
          ploty[1][0] = Math.log10(force_n_to_display_units(current_part.drag_profile));
          ploty[2][0] = Math.log10(force_n_to_display_units(current_part.drag_aux));
          ploty[3][0] = Math.log10(force_n_to_display_units(current_part.drag_junc));
          ploty[4][0] = Math.log10(force_n_to_display_units(current_part.drag_spray));
          
          for (spd = end_spd, ic=npt; ic >= 1; ic--, spd-=del) {
            velocity = spd;
            //? vpp.steady_flight_at_given_speed(5, craft_pitch);
            vpp.steady_flight_at_given_speed(5, 0);
            // con.recomp_all_parts(); // just in case

            plotx[0][ic] = 
            plotx[1][ic] = 
            plotx[2][ic] = 
            plotx[3][ic] = 
            plotx[4][ic] = 
              speed_kmh_to_display_units(velocity);

            ploty[0][ic] = Math.log10(force_n_to_display_units(current_part.drag)); // total
            ploty[1][ic] = Math.log10(force_n_to_display_units(current_part.drag_profile));
            ploty[2][ic] = Math.log10(force_n_to_display_units(current_part.drag_aux));
            ploty[3][ic] = Math.log10(force_n_to_display_units(current_part.drag_junc));
            ploty[4][ic] = Math.log10(force_n_to_display_units(current_part.drag_spray));

          }

          velocity = saved_velocity;
          vpp.steady_flight_at_given_speed(5, 0);
          can_do_gui_updates = saved_flag;
          // this is needed for imported foils so that outline is recomputed
          solver.compute_foil_geometry(effective_aoa());
            
          break;
        }

        case PLOT_TYPE_DRAG_TOTALS_VS_SPEED: { // various drag sources versus speed
          ntiky = 7;
          laby = current_display_force_unit_string();
          labyu = "";

          npt = 40;
          plot_trace_count = 5;
          axis_x_label_width = 5;  axis_y_label_width = 3;

          labx = String.valueOf("Speed");
          // spd = Math.min(8, velocity);
          spd = 8;
          labxu = current_display_speed_unit_string();
          double beg_spd = 5;
          double end_spd = 50;
          del = (npt == 1) ? 0 : (end_spd-beg_spd)/(npt-1);

          boolean saved_flag = can_do_gui_updates;
          can_do_gui_updates = false;;
          //con.recomp_all_parts(); // only make sure we sync cache with saved part props
          double saved_velocity = velocity;

          plotx[0][0] = plotx[1][0]  = plotx[2][0] = plotx[3][0] = plotx[4][0] = 
            speed_kmh_to_display_units(velocity);

          ploty[0][0] = Math.log10(force_n_to_display_units(wing.drag));
          ploty[1][0] = Math.log10(force_n_to_display_units(stab.drag));
          ploty[2][0] = Math.log10(force_n_to_display_units(strut.drag));
          ploty[3][0] = Math.log10(force_n_to_display_units(fuse.drag));
          ploty[4][0] = Math.log10(force_n_to_display_units
                                   (wing.drag_junc + stab.drag_junc + strut.drag_junc + fuse.drag_junc));
          

          for (spd = end_spd, ic=npt; ic >= 1; ic--, spd-=del) {
            velocity = spd;
            //? vpp.steady_flight_at_given_speed(5, craft_pitch);
            vpp.steady_flight_at_given_speed(5, 0);
            // con.recomp_all_parts(); // just in case

            plotx[0][ic] = 
            plotx[1][ic] = 
            plotx[2][ic] = 
            plotx[3][ic] = 
            plotx[4][ic] = 
              speed_kmh_to_display_units(velocity);

            ploty[0][ic] = Math.log10(force_n_to_display_units(wing.drag));
            ploty[1][ic] = Math.log10(force_n_to_display_units(stab.drag));
            ploty[2][ic] = Math.log10(force_n_to_display_units(strut.drag));
            ploty[3][ic] = Math.log10(force_n_to_display_units(fuse.drag));
            ploty[4][ic] = Math.log10(force_n_to_display_units
                                   (wing.drag_junc + stab.drag_junc + strut.drag_junc + fuse.drag_junc));
          }

          velocity = saved_velocity;
          vpp.steady_flight_at_given_speed(5, 0);
          can_do_gui_updates = saved_flag;
          // this is needed for imported foils so that outline is recomputed
          solver.compute_foil_geometry(effective_aoa());
            

          break;
        }

        case PLOT_TYPE_ALTITUDE: { // lift and drag versus altitude
            npt = 20;
            plot_trace_count = 1;
            axis_x_label_width = 6;  axis_y_label_width = 3;
            begx=0.0; 
            endx=50.0; 
            ntikx=6;
            if (lunits == IMPERIAL) endx = 50.0;
            if (lunits == 1) endx = 15.0;
            labx = String.valueOf("Altitude");
            if (lunits == IMPERIAL) labxu = String.valueOf("k-ft");
            if (lunits == 1) labxu = String.valueOf("km");
            del = altmax / npt;
            for (ic=1; ic <=npt; ++ic) {
              hpl = (ic-1)*del;
              plotx[0][ic] = lconv*hpl/1000.;
              tpl = 518.6;
              ppl = 2116.;
              if (planet == 0) {
                if (hpl < 36152.)   {
                  tpl = 518.6 - 3.56 * hpl /1000.;
                  ppl = 2116. * Math.pow(tpl/518.6, 5.256);
                }
                else {
                  tpl = 389.98;
                  ppl = 2116. * .236 * Math.exp((36000.-hpl)/(53.35*tpl));
                }
                if (plot_y_val_2 == 0) 
                  ploty[0][ic] = lftref * ppl/(tpl*53.3*32.17) / rho_EN;
                else
                  ploty[0][ic] = drgref * ppl/(tpl*53.3*32.17) / rho_EN;
              }
              if (planet == 1) {
                if (hpl <= 22960.) {
                  tpl = 434.02 - .548 * hpl/1000.;
                  ppl = 14.62 * Math.pow(2.71828,-.00003 * hpl);
                }
                if (hpl > 22960.) {
                  tpl = 449.36 - 1.217 * hpl/1000.;
                  ppl = 14.62 * Math.pow(2.71828,-.00003 * hpl);
                }
                if (plot_y_val_2 == 0) 
                  ploty[0][ic] = lftref * ppl/(tpl*1149.) / rho_EN;
                else 
                  ploty[0][ic] = drgref * ppl/(tpl*1149.) / rho_EN;
              }
              if (planet == 2) {
                if (plot_y_val_2 == 0) 
                  ploty[0][ic] = lftref;
                else 
                  ploty[0][ic] = drgref;
              }
            }
            ntiky = 5;
            if (plot_y_val_2 == 0) laby = String.valueOf("Lift");
            if (plot_y_val_2 == 1) laby = String.valueOf("Drag");
            plotx[1][0] = alt/100.;
            if (plot_y_val_2 == 0) ploty[1][0] = lftref;
            if (plot_y_val_2 == 1) ploty[1][0] = drgref;
            if (lunits == IMPERIAL) labyu = String.valueOf("lbs");
            if (lunits == 1) labyu = String.valueOf("N");
          
            break;
        }
        case PLOT_TYPE_WING_AREA: { // lift and drag versus area
            npt = 2;
            plot_trace_count = 1;
            axis_x_label_width = 7;  axis_y_label_width = 3;
            begx=0.0; ntikx=6;
            labx = String.valueOf("Area ");
            if (lunits == IMPERIAL) {
              labxu = String.valueOf("sq ft");
              endx = 2000.0;
              labyu = String.valueOf("lbs");
              plotx[0][1] = 0.0;
              ploty[0][1] = 0.0;
              plotx[0][2] = 2000.;
              if (plot_y_val_2 == 0) ploty[0][2] = lftref * 2000. /current_part.area;
              if (plot_y_val_2 == 1) ploty[0][2] = drgref * 2000. /current_part.area;
            }
            if (lunits == 1) {
              labxu = String.valueOf("sq m");
              endx = 200.;
              labyu = String.valueOf("N");
              plotx[0][1] = 0.0;
              ploty[0][1] = 0.0;
              plotx[0][2] = 200.;
              if (plot_y_val_2 == 0) ploty[0][2] = lftref * 200. /current_part.area; 
              if (plot_y_val_2 == 1) ploty[0][2] = drgref * 200. /current_part.area; 
            }

            ntiky = 5;
            plotx[1][0] = current_part.area;
            if (plot_y_val_2 == 0) {
              laby = String.valueOf("Lift");
              ploty[1][0] = lftref;
            }
            else {
              laby = String.valueOf("Drag");
              ploty[1][0] = drgref;
            }
          
            break;
        }
        case PLOT_TYPE_DENSITY: { // lift and drag versus density
            npt = 2;
            plot_trace_count = 1;
            axis_x_label_width = 7; axis_y_label_width = 3;
            begx=0.0; ntikx=6;
            labx = String.valueOf("Density ");
            if (planet == 0) {
              if (lunits == IMPERIAL) {
                labxu = String.valueOf("x 10,000 slug/cu ft");
                endx = 25.0;
                plotx[0][1] = 0.0;
                ploty[0][1] = 0.0;
                plotx[0][2] = 23.7;
                if (plot_y_val_2 == 0) ploty[0][2] = lftref * 23.7 /(rho_EN*10000.);
                if (plot_y_val_2 == 1) ploty[0][2] = drgref * 23.7 /(rho_EN*10000.);
                plotx[1][0] = rho_EN*10000.;
              }
              if (lunits == 1) {
                labxu = String.valueOf("g/cu m");
                endx = 1250.;
                plotx[0][1] = 0.0;
                ploty[0][1] = 0.0;
                plotx[0][2] = 1226;
                if (plot_y_val_2 == 0) ploty[0][2] = lftref * 23.7 /(rho_EN*10000.);
                if (plot_y_val_2 == 1) ploty[0][2] = drgref * 23.7 /(rho_EN*10000.);
                plotx[1][0] = rho_EN*1000.*515.4;
              }
            }

            if (planet == 1) {
              if (lunits == IMPERIAL) {
                labxu = String.valueOf("x 100,000 slug/cu ft");
                endx = 5.0;
                plotx[0][1] = 0.0;
                ploty[0][1] = 0.0;
                plotx[0][2] = 2.93;
                if (plot_y_val_2 == 0) ploty[0][2] = lftref * 2.93 /(rho_EN*100000.);
                if (plot_y_val_2 == 1) ploty[0][2] = drgref * 2.93 /(rho_EN*100000.);
                plotx[1][0] = rho_EN*100000.;
              }
              if (lunits == 1) {
                labxu = String.valueOf("g/cu m");
                endx = 15.;
                plotx[0][1] = 0.0;
                ploty[0][1] = 0.0;
                plotx[0][2] = 15.1;
                if (plot_y_val_2 == 0) ploty[0][2] = lftref * 2.93 /(rho_EN*100000.);
                if (plot_y_val_2 == 1) ploty[0][2] = drgref * 2.93 /(rho_EN*100000.);
                plotx[1][0] = rho_EN*1000.*515.4;
              }
            }
            ntiky = 5;
            if (plot_y_val_2 == PLOT_OUT_2_LIFT) {
              laby = String.valueOf("Lift");
              ploty[1][0] = lftref;
            } else if (plot_y_val_2 == PLOT_OUT_2_DRAG) { 
              laby = String.valueOf("Drag");
              ploty[1][0] = drgref;
            }

            labyu = current_display_force_unit_string();
            break;
        }
        case PLOT_TYPE_LIFT_DRAG_POLARS: { // lift/drag polar
            npt = 40;
            plot_trace_count = 3;
            axis_x_label_width = 4;  axis_y_label_width = 3;
            ntikx=5;
            del = 40.0 / npt;

            for (ic=1; ic <=npt; ++ic) {
              angl = -20.0 + (ic-1)*del;
              double clpl = getCl_plot(current_part.camber/25,current_part.thickness/25,angl);
              boolean ar_lift_corr_saved = ar_lift_corr;
              ar_lift_corr = true;
              ar_lift_corr = ar_lift_corr_saved;
              double cl2d = f.get_Cl(angl);
              ploty[0][ic] = cl2d;
              ploty[1][ic] = clpl;
              ploty[2][ic] = clpl;
              alfd = angl;
              double thkd = current_part.thickness;
              double camd = current_part.camber;
              //   attempt to fix symmetry problem
              if (camd < 0.0) alfd = - angl;
              //
              boolean induced_drag_on_saved = induced_drag_on;
              induced_drag_on = false;
              double cdpl = solver.get_Cd(clpl, alfd, thkd, camd);
              plotx[0][ic] = cdpl; // plot with no induced drag 
              double aspect_rat_corr = 0; // still broken (clpl * clpl)/ (3.1415926 * aspect_rat * current_part.Ci_eff);
              plotx[1][ic] = 
                // no induced drag, apect drag added
                cdpl + aspect_rat_corr;
              // with induced_drag
              induced_drag_on = true;
              cdpl = solver.get_Cd(clpl, alfd, thkd, camd);
              plotx[2][ic] = cdpl + aspect_rat_corr;
              induced_drag_on = induced_drag_on_saved;
            }
            ntiky = 5;
            plotx[1][0] = cdref;
            ploty[1][0] = clref;
            labx = String.valueOf("");
            labxu = String.valueOf("");
            laby = String.valueOf("Cl");
            labyu = String.valueOf("");
            
            break;
        }
        default:;
        }

        // determine y - range, aux comp
        switch (plot_type) {
        case PLOT_TYPE_ANGLE: 
          switch (plot_y_val) {
          case PLOT_OUT_CL:
            if (current_part == wing) {
              begy = -1.5;
              endy = 1.5;
            } else if (current_part == fuse) {
              begy = -0.2;
              endy = 0.2;
            } else {
              begy = -1.2;
              endy = 1.2;
            }
            break;
          case PLOT_OUT_CD:
            begy = 0;
            endy = 0.3;
            break;
          default: // need to find min and max
            begy = 1e9;
            endy = -1e9;
            for (index =1; index <= npt; ++ index) {
              if (ploty[0][index] > endy) endy = ploty[0][index];
              if (ploty[0][index] < begy) begy = ploty[0][index];
            }
          }
          break;
        case PLOT_TYPE_THICKNESS: 
        case PLOT_TYPE_CAMBER: {  // determine y - range zero in middle
         if (plot_y_val == PLOT_OUT_LIFT || plot_y_val == PLOT_OUT_CL) {
            if (ploty[0][npt] >= ploty[0][1]) {
              begy=0.0;
              if (ploty[0][1]   > endy) endy = ploty[0][1];
              if (ploty[0][npt] > endy) endy = ploty[0][npt];
              if (endy <= 0.0) {
                begy = ploty[0][1];
                endy = ploty[0][npt];
              }
            }
            if (ploty[0][npt] < ploty[0][1]) {
              endy = 0.0;
              if (ploty[0][1]   < begy) begy = ploty[0][1];
              if (ploty[0][npt] < begy) begy = ploty[0][npt];
              if (begy <= 0.0) {
                begy = ploty[0][npt];
                endy = ploty[0][1];
              }
            }
          }
          else {
            begy = 0.0;
            endy = 0.0;
            for (index =1; index <= npt; ++ index) {
              if (ploty[0][index] > endy) endy = ploty[0][index];
            }
          }
          break;
        }
        case PLOT_TYPE_DRAG_TOTALS_VS_SPEED: 
        case PLOT_TYPE_CURR_PART_VS_SPEED: { //
          begx= 0;
          switch (display_units) {
          case IMPERIAL: endx = 40; ntikx = 9; break;
          default:
          case METRIC  : endx = 15; ntikx = 6; break;
          case METRIC_2: endx = 50; ntikx = 11; break;
          case NAVAL:    endx = 30; ntikx = 7; break;
          }
          if (display_units == METRIC) {
            begy = -0.5;
            endy = 2.5;
          } else {
            begy = -1.5;

            endy = 1.5;
          }
          if (should_rescale_p) { 
            for (int plot_idx = 0; plot_idx < plot_trace_count; plot_idx++) {
              int start_idx = (plot_idx == 0) ? 0 : 1;
              for (index = 0; index <= npt; ++ index) {
                double val = ploty[plot_idx][index];
                if (val > endy) endy = val;
                // log scale, avoid zero or below vals
                if (val < begy && val != Double.NEGATIVE_INFINITY) begy = val;
                val = plotx[plot_idx][index];
                if (val > endx) endx = val;
                if (val < begx) begx = val;
              }
            }
          } else {
          }
          break;
        }
        case PLOT_TYPE_CG_VS_SPEED: {    //
          begx= 0;
          switch (display_units) {
          case IMPERIAL: endx = 40; ntikx = 9; break;
          default:
          case METRIC  : endx = 15; ntikx = 6; break;
          case METRIC_2: endx = 50; ntikx = 11; break;
          case NAVAL:    endx = 30; ntikx = 7; break;
          }
          if (should_rescale_p) { // button Rescale was just pressed...
            begy = -20;
            endy = 50;
            for (int plot_idx = 0; plot_idx < plot_trace_count; plot_idx++) {
              for (index = 0; index <= npt; ++ index) {
                if (index > 0 && index < start_pt) break;
                double val = ploty[plot_idx][index];
                if (val > endy) endy = val;
                if (val < begy) begy = val;
                val = plotx[plot_idx][index];
                if (val > endx) endx = val;
                if (val < begx) begx = val;
              }
            }
          } else {
            begy = -20;
            endy = 50;
          }
          break;
        }
        case PLOT_TYPE_ALTITUDE:
        case PLOT_TYPE_WING_AREA:
        case PLOT_TYPE_DENSITY: {    // determine y - range
          if (ploty[0][npt] >= ploty[0][1]) {
            begy = ploty[0][1];
            endy = ploty[0][npt];
          }
          if (ploty[0][npt] < ploty[0][1]) {
            begy = ploty[0][npt];
            endy = ploty[0][1];
          }
          break;
        }
        case PLOT_TYPE_LIFT_DRAG_POLARS:  { 
          ntikx = 7;
          ntiky = 7;
          // Drag Polar determine y - range and x- range
          if (should_rescale_p) { // button Rescale was just pressed...
            begx = 0.0;
            endx = 0.0;
            for (index = 1; index <= npt; ++ index) {
              if (plotx[0][index] > endx) endx = plotx[0][index];
            }
            endx = ((10*(int)Math.round(endx*10))/(ntikx-1))*6/100.0;
          
            begy = 10;
            endy = -10;
            for (index =1; index <= npt; ++ index) {
              if (ploty[0][index] < begy) begy = ploty[0][index];
              if (ploty[0][index] > endy) endy = ploty[0][index];
            }
            begy = (Math.round(10*begy)-1)/10.0;
            endy = (Math.round(10*endy)+1)/10.0;
          } else { // predef scale

            begx = 0;
            endx = 0.12; // we want 1st tick as 0.02 
            if (FoilBoard.this.current_part == FoilBoard.this.wing) {
              begy = -1; 
              endy = 2;
            // } else if (FoilBoard.this.current_part == FoilBoard.this.stab) {
            //   begy = -1.5; 
            //   endy = 1.5;
            } else if (FoilBoard.this.current_part == FoilBoard.this.fuse) {
              begy = -0.4; 
              endy = 0.4;
            } else {
              begy = -1.5; 
              endy = 1.5;
            }
          }
          break;
        }

        case PLOT_TYPE_PRESSURE:
        case PLOT_TYPE_VELOCITY: {    // determine y - range
          if (true /*always rescale should_rescale_p*/) {
            begy = ploty[0][1];
            endy = ploty[0][1];
            for (index = 1; index <= POINTS_COUNT_HALF; ++ index) {
              if (ploty[0][index] < begy) begy = ploty[0][index];
              if (ploty[1][index] < begy) begy = ploty[1][index];
              if (ploty[0][index] > endy) endy = ploty[0][index];
              if (ploty[1][index] > endy) endy = ploty[1][index];
            }
          }
        }          
        default:;
        }         

      }


      // camb_val and thic_val are in %/25, angl is in degrees
      public double getCl_plot (double camb_val, double thic_val, double angl) {
        // return new Solver(angl, thic_val*25, camb_val*25).get_Cl(angl);
        Solver s = plot_solver;
        s.getFreeStream ();
        s.getCirculation(angl, thic_val*25, camb_val*25);
        double cl = s.get_Cl(angl);
        // System.out.println("-- s: " + s + " cl: " + cl);
        return cl;
      }
      
      // // old. this was replica of solver.getCirculation & get_Cl
      // public double getCl_plot_(double camb, double thic, double angl) {
      //   double number;
      //   double yc = camb / 2.0;
      //   double rc = thic/4.0 + Math.sqrt( thic*thic/16.0 + yc*yc + 1.0);
      //   double xc = 1.0 - Math.sqrt(rc*rc - yc*yc);
      //   double beta = Math.asin(yc/rc)/convdr;       /* Kutta condition */
      //   double gamc = 2.0*rc*Math.sin((angl+beta)*convdr);
      //   double lec = xc - Math.sqrt(rc*rc - yc*yc);
      //   double tec = xc + Math.sqrt(rc*rc - yc*yc);
      //   double lecm = lec + 1.0/lec;
      //   double tecm = tec + 1.0/tec;
      //   double crdc = tecm - lecm;
      // 
      //   // stall model 1
      //   double stfact = 1.0;
      // 
      //   switch (stall_model_type) {
      //   case STALL_MODEL_DFLT:
      //     if (angl > 10 ) {
      //       stfact = .5 + .1 * angl - .005 * angl * angl;
      //     } else if (angl < -10 ) {
      //       stfact = .5 - .1 * angl - .005 * angl * angl;
      //     }
      //     break;
      //   case STALL_MODEL_REFINED:
      //     // stfact = stall_model_k0 + stall_model_k1 * Math.abs(angl) + stall_model_k2 * angl * angl;
      //     if (angl > stall_model_apos ) {
      //       stfact = stall_model_k0 + stall_model_k1 * +angl + stall_model_k2 * angl * angl;
      //     } else if (angl < stall_model_aneg ) {
      //       stfact = stall_model_k0 + stall_model_k1 * -angl + stall_model_k2 * angl * angl;
      //     }
      //   break;
      //   case STALL_MODEL_IDEAL_FLOW:
      //   default:
      //     // do nothing. stfact = 1
      //   }
      // 
      //   number = stfact*gamc*4.0*3.1415926/crdc;
      // 
      //   if (ar_lift_corr) {  // correction for low aspect ratio
      //     number = number /(1.0 + Math.abs(number)/(3.14159*aspect_rat));
      //   }
      // 
      //   return (number);
      // }
   
      public void update(Graphics g) {
        out.plot.paint(g);
      }

      static final boolean GRIDLINES = true;

      // class Plot
      public void paint (Graphics g) {
        int i,j,k,n,index;
        int xlabel,ylabel,ind,inmax,inmin;
        int x[] = new int[8];
        int y[] = new int[8];
        double offx,scalex,offy,scaley,waste,incy,incx;
        double xl,yl;
        double liftab,dragab;
        Color col;


        if (ntikx < 2) ntikx = 2;     /* protection 13June96 */
        incx = (endx-begx)/(ntikx-1);
        if (ntiky < 2) ntiky = 2;
        incy = (endy-begy)/(ntiky-1);

        offx = 0.0 - begx;
        offy = 0.0 - begy;


        // resize everything to panel size
        //
        // note 1 FS3 defaults: xtp = 95; ytp = 165; factp = 30.0;
        // note 2 FS3 maintained x/y scaling as 6/4.5 or 2/1.5
        // factp wass elected 30 so that gave pixel sizes of the plot 180/135
        // scalex = 6.0/(endx-begx);
        // scaley = 4.5/(endy-begy);

        int panel_height = getHeight();
        int panel_width = getWidth();
        // System.out.println("-- panel_height: " + panel_height);
        
        // x0, y: screen coordinates of plot left bottom corner. 
        int x0 = 48+ 8*Math.max(laby.length(), labyu.length()); // leave enough room for the Y axis labels
        int y0 = panel_height - 64; /// leave space at bottom for the buttons and legend
        int plot_h = y0 - 20; // leave space above for title etc
        int plot_w = panel_width - x0 - 20;
        double plot_AR = plot_w/plot_h;
        scalex = plot_w/(endx-begx);
        scaley = plot_h/(endy-begy);
 
        if (plot_type != PLOT_TYPE_GAGES)  { /*  draw a graph */

          if (GRIDLINES // let always paint on black for grid lines
              || plot_type == PLOT_TYPE_PRESSURE ||  plot_type == PLOT_TYPE_VELOCITY)
            off2Gg.setColor(color_very_dark);
          else
            off2Gg.setColor(Color.blue);

          off2Gg.fillRect(0,0,panel_width,panel_height);

          off2Gg.setColor(Color.white);
          // /* draw axes */
          // x[0] = x0;          y[0] = y0 - plot_h;  // Y axis end
          // x[1] = x0;          y[1] = y0;           // x, y origin
          // x[2] = x0 + plot_w; y[2] = y0;           // X axis end
          // off2Gg.drawLine(x[0],y[0],x[1],y[1]);
          // off2Gg.drawLine(x[1],y[1],x[2],y[2]);
 
          off2Gg.drawString(laby, 2, y0 - plot_h/2); // Y axis label 1
          off2Gg.drawString(labyu,2, y0 - plot_h/2 + 10); // Y axis label 2
          boolean log_drag_p = plot_type == PLOT_TYPE_DRAG_TOTALS_VS_SPEED || plot_type == PLOT_TYPE_CURR_PART_VS_SPEED;
          /* add Y axis tick values and horiz ticks or grid lines */
          for (ind= 1; ind<= ntiky; ++ind){
            xlabel = x0 - 40;
            yl = begy + (ind-1) * incy;
            int y_tick = (int) ( -scaley*(yl + offy)) + y0;
            if (ind == 1 || ind == ntiky)  { // draw axes and bounds
              // redundant? off2Gg.setColor(Color.WHITE);  
              off2Gg.drawLine(x0, y_tick, x0+plot_w, y_tick);
            } else if (!GRIDLINES) /// draw a tick
              off2Gg.drawLine(x0-4, y_tick, x0+6, y_tick);
            else { // drag dimmed grid line_dark
              off2Gg.setColor(Color.GRAY);
              off2Gg.drawLine(x0, y_tick, x0+plot_w, y_tick);
              off2Gg.setColor(Color.WHITE);
            }
            ylabel = y_tick+4;//(int) ( -scaley*(yl + offy)) + y0;

            // maybe log scale?
            if (log_drag_p) 
              yl = Math.pow(10, yl);

            // old ways
            // if (axis_y_label_width >= 2) {
            //   if (endy >= 100.0)
            //     off2Gg.drawString(pprint(filter0(yl)),xlabel,ylabel);
            //   else if (endy < 100.0 && endy >= 1.0) 
            //     off2Gg.drawString(pprint(filter1(yl)),xlabel,ylabel);
            //   else if (endy < 1.0) 
            //     off2Gg.drawString(pprint(filter3(yl)),xlabel,ylabel);
            // }
            // else {
            //   off2Gg.drawString(pprint(filter3(yl)),xlabel,ylabel);
            // }
            off2Gg.drawString(pprint(filter1or3(yl)),xlabel,ylabel);

          }

          if (log_drag_p) {
            off2Gg.drawString(labx, x0 + plot_w - 40, y0 - 16);
            off2Gg.drawString(labxu,x0 + plot_w - 40, y0 - 6);
          } else {
            off2Gg.drawString(labx, x0 + plot_w - 60, y0 + 30);
            off2Gg.drawString(labxu,x0 + plot_w - 60, y0 + 40);
          }

          /* add X axis tick values and either vertical ticks or grid lines */
          for (ind= 1; ind<= ntikx; ++ind){
            ylabel = (int) (15.) + y0;
            xl = begx + (ind-1) * incx;
            int x_tick = (int) ((scalex*(xl + offx))) + x0;
            if (ind == 1 || ind == ntikx) // draw axe and bound
              off2Gg.drawLine(x_tick, y0, x_tick, y0-plot_h);
            else if (!GRIDLINES) // tick
              off2Gg.drawLine(x_tick, y0-6, x_tick, y0+4);
            else { // drag dimmed grid line
              off2Gg.setColor(Color.GRAY);
              off2Gg.drawLine(x_tick, y0, x_tick, y0-plot_h);
              off2Gg.setColor(Color.WHITE);
            }

            xlabel = xlabel = x_tick - 8;// (int) ((scalex*(xl + offx) -.05)) + x0;

            // old ways...
            // if (axis_x_label_width == 1) {
            //   off2Gg.drawString(String.valueOf(xl),xlabel,ylabel);
            // } else if (endx >= 10)
            //   off2Gg.drawString(String.valueOf((int)xl),xlabel,ylabel);
            // else
            //   off2Gg.drawString(pprint(filter3(xl)),xlabel,ylabel);
            off2Gg.drawString(pprint(filter1or3(xl)),xlabel,ylabel);            

          }
       
          if (lines == 0) {
            for (i=1; i<=npt; ++i) {
              xlabel = (int) (scalex*(offx+plotx[0][i])) + x0;
              ylabel = (int)(-scaley*(offy+ploty[0][i]) +7.)+y0;
              off2Gg.drawString("*",xlabel,ylabel);
            }
          }
          else {
            switch (plot_type) {
            case PLOT_TYPE_PRESSURE:
            case PLOT_TYPE_VELOCITY: {
              for (j=0; j<= plot_trace_count-1; ++j) {
                k = 2 -j;
                if (k == 0) {
                  off2Gg.setColor(Color.magenta);
                  xlabel = x0 + 20;
                  ylabel = y0 + 20;
                  off2Gg.drawString("Upper",xlabel,ylabel);
                }
                else if (k == 1) {
                  off2Gg.setColor(Color.yellow);
                  xlabel = x0 + 20;
                  ylabel = y0 + 40;
                  off2Gg.drawString("Lower",xlabel,ylabel);
                }
                else if (k == 2) {
                  off2Gg.setColor(Color.green);
                  xlabel = x0 +  plot_w/2 - 8*"Free St".length();
                  ylabel = y0 -  plot_h - 4;
                  off2Gg.drawString("Free Stream",xlabel,ylabel);
                }
                x[1] = (int) (scalex*(offx+plotx[k][1])) + x0;
                y[1] = (int) (-scaley*(offy+ploty[k][1]))+ y0;
                for (i=1; i<=npt; ++i) {
                  x[0] = x[1];
                  y[0] = y[1];
                  x[1] = (int)(scalex*(offx+plotx[k][i]))+x0;
                  y[1] = (int)(-scaley*(offy+ploty[k][i]))+y0;
                  off2Gg.drawLine(x[0],y[0],x[1],y[1]);
                }
              }
              break;
            } // PLOT_TYPE_VELOCITY 
            case PLOT_TYPE_CG_VS_SPEED: {

              
              off2Gg.drawString("Equilibrium conditions",
                                x0 +  plot_w/2 - 50,
                                y0 -  plot_h - 4);


              int threshold_x = (int)(+scalex*(offx+plotx[1][start_pt])) + x0;
              off2Gg.drawString("<--- --- Rideable speed range --- --- ", threshold_x, y0 -  plot_h + 20);
              off2Gg.setColor(Color.red);
              off2Gg.drawLine(threshold_x - 3, 
                              y0 -  plot_h - 4,
                              threshold_x - 3,
                              y0);
              off2Gg.drawString("Slog/Swim -->", threshold_x - 80, y0 -  plot_h + 20);
              off2Gg.drawString("range", threshold_x - 80, y0 -  plot_h + 20+14);
              
              
              String cg_plot_label = "";
              for (j=0; j< plot_trace_count; ++j) {
                int cg_plot_label_x = x0 + 24;
                int cg_plot_label_y = y0;
                switch (j) {
                case 0:
                  off2Gg.setColor(Color.yellow);
                  cg_plot_label = (display_units == METRIC || display_units == METRIC_2) 
                    ? "CG pos, cm"
                    : "CG pos, inch";
                  cg_plot_label_x += 4;
                  cg_plot_label_y += 32;
                  break;
                case 1:
                  off2Gg.setColor(Color.magenta);
                  cg_plot_label = (display_units == METRIC)
                    ? "Drag, kg (or 9.8*N), to fit the screen"
                    : ("Drag, " + current_display_force_unit_string());
                  cg_plot_label_x += 4;
                  cg_plot_label_y += 32+14;
                  break;
                case 2: 
                  off2Gg.setColor(Color.green);
                  cg_plot_label = "Pitch, Degr";
                  cg_plot_label_x += 4;
                  cg_plot_label_y += 32+14+14;
                  break;
                case 3: 
                  off2Gg.setColor(Color.cyan);
                  cg_plot_label = "Total L/D ratio";
                  cg_plot_label_x += 104;
                  cg_plot_label_y += 32+14+14;
                  break;
                default:
                }
                off2Gg.drawString(cg_plot_label, cg_plot_label_x, cg_plot_label_y);

                x[1] = (int) (+scalex*(offx+plotx[j][start_pt])) + x0;
                y[1] = (int) (-scaley*(offy+ploty[j][start_pt])) + y0;
                for (i=start_pt+1; i<=npt; ++i) {
                  x[0] = x[1];
                  y[0] = y[1];
                  x[1] = (int) (+scalex*(offx+plotx[j][i])) + x0;
                  y[1] = (int) (-scaley*(offy+ploty[j][i])) + y0;
                  // for debugging off2Gg.drawLine(x[1],y[1]-1, x[1],y[1]+1);
                  off2Gg.drawLine(x[0],y[0],x[1],y[1]);
                }
                xlabel = (int)(+scalex*(offx+plotx[j][0])) + x0;
                ylabel = (int)(-scaley*(offy+ploty[j][0])) + y0;
                //off2Gg.drawLine(xlabel, ylabel-4, xlabel, ylabel+4);
                off2Gg.drawOval(xlabel-2,ylabel-2,4,4);
              }
              break;
            } // PLOT_TYPE_CG_VS_SPEED
            case PLOT_TYPE_CURR_PART_VS_SPEED: 
              off2Gg.drawString("Drag Components for " + current_part.name,
                                x0 +  plot_w/2 - 50,
                                y0 -  plot_h - 4);
              // fall down
            case PLOT_TYPE_DRAG_TOTALS_VS_SPEED: {

              String drag_plot_label = "";
              for (j=0; j< plot_trace_count; ++j) {
                int drag_plot_label_x = x0 + 48;
                int drag_plot_label_y = y0;
                switch (j) {
                case 0:
                  if (plot_type == PLOT_TYPE_DRAG_TOTALS_VS_SPEED) {
                      off2Gg.setColor(Color.yellow);
                      drag_plot_label = "Drag of Wing";
                  } else { // PLOT_TYPE_CURR_PART_VS_SPEED
                      off2Gg.setColor(Color.red);
                      drag_plot_label = "Total Drag";
                  }
                  drag_plot_label_x += 4;
                  drag_plot_label_y += 32;
                  break;
                case 1:
                  off2Gg.setColor(Color.magenta);
                  if (plot_type == PLOT_TYPE_DRAG_TOTALS_VS_SPEED)
                    drag_plot_label = "Drag of Stab";
                  else
                    drag_plot_label = "Profile Darg";
                  drag_plot_label_x += 4;
                  drag_plot_label_y += 32+14;
                  break;
                case 2: 
                  off2Gg.setColor(Color.green);
                  if (plot_type == PLOT_TYPE_DRAG_TOTALS_VS_SPEED)
                    drag_plot_label = "Drag of Mast";
                  else
                    drag_plot_label = "Induced Drag";
                  drag_plot_label_x += 4;
                  drag_plot_label_y += 32+14+14;
                  break;
                case 3: 
                  off2Gg.setColor(Color.cyan);
                  if (plot_type == PLOT_TYPE_DRAG_TOTALS_VS_SPEED)
                    drag_plot_label = "Drag of Fuse";
                  else
                    drag_plot_label = "Junction Drag";
                  drag_plot_label_x += 114;
                  drag_plot_label_y += 32;
                  break;
                case 4: 
                  if (plot_type == PLOT_TYPE_DRAG_TOTALS_VS_SPEED) {
                    off2Gg.setColor(Color.red);
                    drag_plot_label = "All Parts Junction+Spray Drag";
                  } else {
                    off2Gg.setColor(Color.yellow);
                    drag_plot_label = "Spray Drag";
                  }
                  drag_plot_label_x += 114;
                  drag_plot_label_y += 32+14;
                  break;
                default:
                }
                off2Gg.drawString(drag_plot_label, drag_plot_label_x, drag_plot_label_y);

                x[1] = (int) (+scalex*(offx+plotx[j][1])) + x0;
                y[1] = (int) (-scaley*(offy+ploty[j][1])) + y0;
                for (i=1; i<=npt; ++i) {
                  x[0] = x[1];
                  y[0] = y[1];
                  x[1] = (int) (+scalex*(offx+plotx[j][i])) + x0;
                  y[1] = (int) (-scaley*(offy+ploty[j][i])) + y0;
                  // for debugging off2Gg.drawLine(x[1],y[1]-1, x[1],y[1]+1);
                  off2Gg.drawLine(x[0],y[0],x[1],y[1]);
                }
                xlabel = (int)(+scalex*(offx+plotx[j][0])) + x0;
                ylabel = (int)(-scaley*(offy+ploty[j][0])) + y0;
                //off2Gg.drawLine(xlabel, ylabel-4, xlabel, ylabel+4);
                off2Gg.drawOval(xlabel-2,ylabel-2,4,4);
              }
              break;
            }
            case PLOT_TYPE_LIFT_DRAG_POLARS: // fall through here
              off2Gg.setColor(plot_colors[0]);
              off2Gg.drawString("Cl/Cd of a 2D (infinite) foil", x0+plot_w/3, y0+32);
              off2Gg.setColor(plot_colors[1]);
              off2Gg.drawString("Cl corrected for Aspect Ratio", x0+plot_w/3, y0+32+14);
              off2Gg.setColor(plot_colors[2]);
              off2Gg.drawString("that, and Cd with induced drag added", x0+plot_w/3, y0+32+14+14);
              // fall through! no break!
            default: 
              for (j=0; j<= plot_trace_count-1; ++j) {
                x[1] = (int) (scalex*(offx+plotx[j][1])) + x0;
                y[1] = (int) (-scaley*(offy+ploty[j][1])) + y0;
                off2Gg.setColor(plot_colors[j]);
                for (i=2; i<=npt; ++i) {
                  x[0] = x[1];
                  y[0] = y[1];
                  x[1] = (int) (scalex*(offx+plotx[j][i])) + x0;
                  y[1] = (int) (-scaley*(offy+ploty[j][i])) + y0;
                  off2Gg.drawLine(x[0],y[0],x[1],y[1]);
                }
              }
              xlabel = (int) (scalex*(offx+plotx[1][0])) + x0;
              ylabel = (int)(-scaley*(offy+ploty[1][0]))+y0 -4;
              off2Gg.setColor(Color.red);
              off2Gg.fillOval(xlabel,ylabel,5,5);
            }
          }

          // Rescale button
          off2Gg.setColor(should_rescale_p ? Color.yellow : Color.white);
          off2Gg.fillRect(4, (rescale_bt_y = panel_height -24), 68, 20);
          off2Gg.setColor(Color.red);
          off2Gg.drawString("Rescale",8,panel_height-8);

        } else { /*  draw the lift and drag gauge */
          off2Gg.setColor(color_very_dark);
          off2Gg.fillRect(0,0,350,350);
          off2Gg.setColor(Color.white);
          off2Gg.drawString("Output",10,10);
          off2Gg.setColor(color_very_dark);
          off2Gg.fillRect(0,100,300,30);
          // Thermometer Lift gage
          off2Gg.setColor(Color.white);
          if (lftout == 0) {
            off2Gg.drawString("Lift =",70,75);
            if (lunits == IMPERIAL) off2Gg.drawString("Pounds",190,75);
            else off2Gg.drawString("Newtons",190,75);
          }
          else if (lftout == 1) 
            off2Gg.drawString(" Cl  =",70,185);
          // Thermometer Drag gage
          if (dragOut == 0) {
            off2Gg.drawString("Drag =",70,185);
            if (lunits == IMPERIAL) off2Gg.drawString("Pounds",190,185);
            if (lunits == 1) off2Gg.drawString("Newtons",190,185);
          }
          if (dragOut == 1) off2Gg.drawString(" Cd  =",70,185);

          off2Gg.setColor(Color.yellow);
          for (index=0; index <= 10; index ++) {
            off2Gg.drawLine(7+index*25,100,7+index*25,110);
            off2Gg.drawString(String.valueOf(index),5+index*25,125);
            off2Gg.drawLine(7+index*25,130,7+index*25,140);
          }
          // Lift value
          if (lftout == 0) {
            liftab = current_part.lift;
            double lift_abs = Math.abs(current_part.lift);
            if (lift_abs <= 1.0) {
              liftab *=10.0;
              off2Gg.setColor(Color.cyan);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("-1",180,70);
            }
            else if (lift_abs > 1.0 && lift_abs <= 10.0) {
              off2Gg.setColor(Color.yellow);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("0",180,70);
            }
            else if (lift_abs > 10.0 && lift_abs <=100.0) {
              liftab /=10.0;
              off2Gg.setColor(Color.green);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("1",180,70);
            }
            else if (lift_abs > 100.0 && lift_abs <=1000.0) {
              liftab /= 100.0;
              off2Gg.setColor(Color.red);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("2",180,70);
            }
            else if (lift_abs > 1000.0 && lift_abs <=10000.0) {
              liftab /= 1000.0;
              off2Gg.setColor(Color.magenta);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("3",180,70);
            }
            else if (lift_abs > 10000.0 && lift_abs <=100000.0) {
              liftab /= 10000.0;
              off2Gg.setColor(Color.orange);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("4",180,70);
            }
            else if (lift_abs > 100000.0 && lift_abs <=1000000.0) {
              liftab /= 100000.0;
              off2Gg.setColor(Color.white);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("5",180,70);
            }
            else if (lift_abs > 1000000.0) {
              liftab /= 1000000.0;
              off2Gg.setColor(Color.white);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("6",180,70);
            }
          } else {
            liftab = current_part.cl;
            if (Math.abs(current_part.cl) <= 1.0) {
              liftab = current_part.cl*10.0;
              off2Gg.setColor(Color.cyan);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("-1",180,70);
            }
            if (Math.abs(current_part.cl) > 1.0 && Math.abs(current_part.cl) <= 10.0) {
              liftab = current_part.cl;
              off2Gg.setColor(Color.yellow);
              off2Gg.fillRect(0,100,7 + (int) (25*Math.abs(liftab)),10);
              off2Gg.drawString("0",180,70);
            }
          }
          // Drag value
          if (dragOut == 0) {
            dragab = current_part.drag;
            double drag_abs = Math.abs(current_part.drag);
            if (drag_abs <= 1.0) {
              dragab *= 10.0;
              off2Gg.setColor(Color.cyan);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("-1",180,180);
            } else if (drag_abs > 1.0 && drag_abs <= 10.0) {
              off2Gg.setColor(Color.yellow);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("0",180,180);
            } else if (drag_abs > 10.0 && drag_abs <=100.0) {
              dragab /= 10.0;
              off2Gg.setColor(Color.green);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("1",180,180);
            } else if (drag_abs > 100.0 && drag_abs <=1000.0) {
              dragab /= 100.0;
              off2Gg.setColor(Color.red);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("2",180,180);
            } else if (drag_abs > 1000.0 && drag_abs <=10000.0) {
              dragab /= 1000.0;
              off2Gg.setColor(Color.magenta);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("3",180,180);
            } else if (drag_abs > 10000.0 && drag_abs <=100000.0) {
              dragab /= 10000.0;
              off2Gg.setColor(Color.orange);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("4",180,180);
            } else if (drag_abs > 100000.0 && drag_abs <=1000000.0) {
              dragab /= 100000.0;
              off2Gg.setColor(Color.white);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("5",180,180);
            } else if (drag_abs > 1000000.0) {
              dragab /= 1000000.0;
              off2Gg.setColor(Color.white);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("6",180,180);
            }
          } else {
            dragab = current_part.cd;
            if (Math.abs(current_part.cd) <= .1) {
              dragab = current_part.cd*100.0;
              off2Gg.setColor(Color.magenta);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("-2",180,180);
            }
            if (Math.abs(current_part.cd) > .1 && Math.abs(current_part.cd) <= 1.0) {
              dragab = current_part.cd*10.0;
              off2Gg.setColor(Color.cyan);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("-1",180,180);
            }
            if (Math.abs(current_part.cd) > 1.0 && Math.abs(current_part.cd) <= 10.0) {
              dragab = current_part.cd;
              off2Gg.setColor(Color.yellow);
              off2Gg.fillRect(0,130,7 + (int) (25*Math.abs(dragab)),10);
              off2Gg.drawString("0",180,180);
            }
          }

          off2Gg.setColor(Color.white);
          off2Gg.drawString(pprint(filter3(liftab)),110,75);
          off2Gg.drawString(" X 10 ",150,75);
          off2Gg.drawString(pprint(filter3(dragab)),110,185);
          off2Gg.drawString(" X 10 ",150,185);
        }  
        g.drawImage(offImg2,0,0,this);   
      }
    }     // Plot 

    class Probe extends Panel {
      FoilBoard app;
      L l;
      R r;

      Probe (FoilBoard target) {

        app = target;
        setLayout(new GridLayout(1,2,5,5));

        l = new L(app);
        r = new R(app);

        add(l);
        add(r);
      }

      class L extends Panel {
        FoilBoard app;
        JLabel l01;
        Button bt1,bt2,bt3;
     
        L (FoilBoard target) {
          app = target;
          setLayout(new GridLayout(4,1,10,10));

          l01 = new JLabel("Probe", JLabel.CENTER);
          l01.setForeground(Color.red);

          bt1 = new_button("Velocity");
          bt2 = new_button("Pressure");
          bt3 = new_button("Smoke");
          
          bt1.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                pboflag = 1;
                bt1.setBackground(Color.yellow);
                bt2.setBackground(Color.white);
                bt3.setBackground(Color.white);
                out.plot.loadPlot();
              }});
          bt2.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                pboflag = 2;
                bt2.setBackground(Color.yellow);
                bt1.setBackground(Color.white);
                bt3.setBackground(Color.white);
                out.plot.loadPlot();
              }});
          bt3.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                pboflag = 3;
                bt3.setBackground(Color.yellow);
                bt2.setBackground(Color.white);
                bt1.setBackground(Color.white);
                out.plot.loadPlot();
              }});

          add(l01);
          add(bt1);
          add(bt2);
          add(bt3);
        }

        public boolean action (Event evt, Object arg) {
          System.out.println("-- warning: obsolete action() invoked... arg: " + arg);
          return true;
        }
      }  // LeftPanel

      class R extends Panel {
        FoilBoard app;
        JScrollBar s1,s2;
        L2 l2;
        Button bt4;

        R (FoilBoard target) {

          app = target;
          setLayout(new BorderLayout(5,5));

          s1 = new JScrollBar(JScrollBar.VERTICAL,550,10,0,1000);
          s2 = new JScrollBar(JScrollBar.HORIZONTAL,550,10,0,1000);

          s1.addAdjustmentListener(new AdjustmentListener() {
              public void adjustmentValueChanged(AdjustmentEvent evt) {
                if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                int i1 = s1.getValue();
                ypval = 5.0 - i1 * 10.0/ 1000.;
                computeFlowAndRegenPlot();
              }});
          s2.addAdjustmentListener(new AdjustmentListener() {
              public void adjustmentValueChanged(AdjustmentEvent evt) {
                if (DEBUG_SPEED_SUPPR_ADJ) { debug_speed_suppr_adj(evt); return;}
                int i2 = s2.getValue();
                xpval = i2 * 20.0/ 1000. -10.0;
                computeFlowAndRegenPlot();
              }});

          l2 = new L2(app);

          bt4 = new Button ("OFF");
          bt4.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                pboflag = 0;
                l.bt3.setBackground(Color.white);
                l.bt2.setBackground(Color.white);
                l.bt1.setBackground(Color.white);
                computeFlowAndRegenPlot();
              }});

          bt4.setBackground(Color.red);
          bt4.setForeground(Color.white);

          add("West",s1);
          add("South",s2);
          add("Center",l2);
          add("North",bt4);
        }

        public boolean handleEvent (Event evt) {
          System.out.println("-- handleEvent: obsolete");
          return false;
        }

        class L2 extends Canvas  {
          FoilBoard app;

          L2 (FoilBoard target) {
            setBackground(color_very_dark);
          }

          public void update (Graphics g) {
            out.probe.r.l2.paint(g);
          }

          public void paint(Graphics g) {
            int ex,ey,index;
            double pbout;
    
            off3Gg.setColor(color_very_dark);
            off3Gg.fillRect(0,0,150,150);

            if (pboflag == 0 || pboflag == 3)off3Gg.setColor(Color.gray);
            if (pboflag == 1 || pboflag == 2)off3Gg.setColor(Color.yellow);
            off3Gg.fillArc(20,30,80,80,-23,227);
            off3Gg.setColor(color_very_dark);
            // tick marks
            for (index = 1; index <= 4; ++ index) {
              ex = 60 + (int) (50.0 * Math.cos(convdr * (-22.5 + 45.0 * index)));
              ey = 70 - (int) (50.0 * Math.sin(convdr * (-22.5 + 45.0 * index)));
              off3Gg.drawLine(60,70,ex,ey);
            }
            off3Gg.fillArc(25,35,70,70,-25,235);
      
            off3Gg.setColor(Color.yellow);
            off3Gg.drawString("0",10,95);
            off3Gg.drawString("2",10,55);
            off3Gg.drawString("4",35,30);
            off3Gg.drawString("6",75,30);
            off3Gg.drawString("8",100,55);
            off3Gg.drawString("10",100,95);

            off3Gg.setColor(Color.green);
            if (pboflag == 1) {
              off3Gg.drawString("Velocity",40,15);
              if (lunits == IMPERIAL) off3Gg.drawString("mph",50,125);
              else /*METRIC*/         off3Gg.drawString("km/h",50,125);
            }
            if (pboflag == 2) {
              off3Gg.drawString("Pressure",30,15);
              if (lunits == IMPERIAL) off3Gg.drawString("psi",50,125);
              else /* METRIC */       off3Gg.drawString("kPa",50,125);
            }

            off3Gg.setColor(Color.green);
            off3Gg.drawString("x 10",65,110);

            ex = 60;
            ey = 70;
               
            pbout = 0.0;
            if (pbval <= .001) {
              pbout = pbval * 1000.;
              off3Gg.drawString("-4",90,105);
            }
            if (pbval <= .01 && pbval > .001) {
              pbout = pbval * 100.;
              off3Gg.drawString("-3",90,105);
            }
            if (pbval <= .1 && pbval > .01) {
              pbout = pbval * 10.;
              off3Gg.drawString("-2",90,105);
            }
            if (pbval <= 1 && pbval > .1) {
              pbout = pbval * 10.;
              off3Gg.drawString("-1",90,105);
            }
            if (pbval <= 10 && pbval > 1) {
              pbout = pbval;
              off3Gg.drawString("0",90,105);
            }
            if (pbval <= 100 && pbval > 10) {
              pbout = pbval * .1;
              off3Gg.drawString("1",90,105);
            }
            if (pbval <= 1000 && pbval > 100) {
              pbout = pbval * .01;
              off3Gg.drawString("2",90,105);
            }
            if (pbval > 1000) {
              pbout = pbval * .001;
              off3Gg.drawString("3",90,105);
            }
            off3Gg.setColor(Color.green);
            off3Gg.drawString(pprint(filter3(pbout)),30,110);

            off3Gg.setColor(Color.yellow);
            ex = 60 - (int) (30.0 * Math.cos(convdr *
                                             (-22.5 + pbout * 225. /10.0)));
            ey = 70 - (int) (30.0 * Math.sin(convdr *
                                             (-22.5 + pbout * 225. /10.0)));
            off3Gg.drawLine(60,70,ex,ey);

            g.drawImage(offImg3,0,0,this);
          }
        } //L2
      }  // RightPanel
    }  // Probe

    class Geometry extends Panel {
      FoilBoard app; GeometryText text;

      public void paint (Graphics g) {
        // System.out.println("-- Geometry paint " + this);
        super.paint(g);
        genReport();
      }

      Geometry (FoilBoard target) {
        setLayout(new GridLayout(1,1,0,0));
        text = new GeometryText(this);
      }

      class GeometryText extends JTextArea {
        GeometryText(Panel panel) { 
          this.setEditable(false);
          this.append("Kite/Windsurf Hydrofoil Simulator V1.0.\nInitially derived from FoilSim III 1.5b beta");
          panel.add(new JScrollPane(this));
        }

        public void paint (Graphics g) {
          // System.out.println("-- CustomTextArea  paint " + this);
          //g.setColor(Color.BLUE);
          //g.drawRect(0, 0, 100, 100);
          super.paint(g);
          // g.setColor(Color.BLUE);
          // g.drawRect(0, 0, 100, 100);
        }
      }

      void genReport () {
        int index;
        double mapfac, volume; 
          
        //??pboflag = 0;
        volume = 0.0;
        
        text.append(current_part.foil.genReportText(current_part.thickness, current_part.camber));

        if (!foil_is_cylinder_or_ball(current_part.foil)) {
          text.append( "\n Camber = " + filter3(current_part.camber) );
          text.append( " % of chord ," );
          text.append( " Thickness = " + filter3(current_part.thickness) );
          text.append( " % chord ," );
          text.append( "\n Chord = " + filter3(current_part.chord) );
          if (lunits == IMPERIAL) text.append( " ft ," );
          else /*METRIC*/         text.append( " m ," );
          text.append( " Span = " + filter3(current_part.span) );
          if (lunits == IMPERIAL) text.append( " ft ," );
          else /*METRIC*/         text.append( " m ," );
          text.append( "\n Angle of attack = " + filter3(current_part.aoa) );
          text.append( " degrees ," );
        } else { // cylinder and ball
          text.append( "\n Spin  = " + filter3(spin*60.0) );
          text.append( " rpm ," );
          text.append( " Radius = " + filter3(radius) );
          if (lunits == IMPERIAL) text.append( " ft ," );
          else /*METRIC*/         text.append( " m ," );
        }
        switch(planet) {
        case 0: {       
          text.append( "\n Standard Earth Atmosphere" );
          break;
        }
        case 1: {       
          text.append( "\n Martian Atmosphere" );
          break;
        }
        case 2: {       
          text.append( "\n Under Water" );
          break;
        }
        case 3: {       
          text.append( "\n Specified Conditions" );
          break;
        }
        case 4: {       
          text.append( "\n Specified Conditions" );
          break;
        }
        }
        switch (lunits)  {
        case 0: {                             /* English */
          text.append( "\n Ambient Pressure = " + filter3(ps0/144.) );
          text.append( "lb/sq in," );
          break;
        }
        case 1: {                             /* Metric */
          text.append( "\n Ambient Pressure = " + filter3(101.3/14.7*ps0/144.) );
          text.append( "kPa," );
          break;
        }
        }
        text.append( "\n Ambient Velocity = " + filter0(velocity) );
        if (lunits == IMPERIAL) text.append( " mph ," );
        else /*METRIC*/         text.append( " km/hr ," );

        text.append( "\n \t Upper Surface \t \t \t ");
        text.append( "\n X/c \t Y/c \t P \t V \t ");
        mapfac = 4.0;
        if (foil_is_cylinder_or_ball(current_part.foil)) mapfac = 2.0;
        for (index = 0; index <= POINTS_COUNT_HALF-1; ++ index) {
          text.append( "\n" + filter3(xpl[0][POINTS_COUNT_HALF-index]/mapfac) +  "\t"
                                        + filter3(ypl[0][POINTS_COUNT_HALF-index]/mapfac) + "\t" + filter3(plp[POINTS_COUNT_HALF-index]) + "\t"
                                        + filter0(plv[POINTS_COUNT_HALF-index]) + "\t"); 
          if (index <= POINTS_COUNT_HALF-2) {
            volume = volume + .5 * (((xpl[0][POINTS_COUNT_HALF-index-1]-xpl[0][POINTS_COUNT_HALF-index])
                                     *(ypl[0][POINTS_COUNT_HALF-index]+ ypl[0][POINTS_COUNT_HALF-index-1])) / (mapfac * mapfac)) * current_part.chord;
          }
        }
        text.append( "\n \t Lower Surface \t \t \t ");
        for (index = 0; index <= POINTS_COUNT_HALF-1; ++ index) {
          text.append( "\n"  + filter3(xpl[0][POINTS_COUNT_HALF+index]/mapfac) + "\t"
                                        + filter3(ypl[0][POINTS_COUNT_HALF+index]/mapfac) + "\t" + filter3(plp[POINTS_COUNT_HALF+index]) + "\t"
                                        + filter0(plv[POINTS_COUNT_HALF+index]) );
          if (index <= POINTS_COUNT_HALF-2) {
            volume = volume - .5 * (((xpl[0][POINTS_COUNT_HALF+index+1]-xpl[0][POINTS_COUNT_HALF+index])
                                     *(ypl[0][POINTS_COUNT_HALF+index]+ ypl[0][POINTS_COUNT_HALF+index+1])) / (mapfac * mapfac)) * current_part.chord;
          }
        }
        volume = volume * current_part.span;
        if (current_part.foil == FOIL_CYLINDER) volume = 3.14159 * radius * radius * current_part.span;
        else if (current_part.foil == FOIL_BALL) volume = 3.14159 * radius * radius * radius * 4.0 / 3.0;
           
        text.append( "\n  Volume =" + filter3(volume));
        if (lunits == IMPERIAL) text.append( " cu ft " );
        else /*METRIC*/         text.append( " cu m " );

        // needed?? computeFlowAndRegenPlotAndAdjust();
      }

    }  // Geometry


    class Data extends Panel {
      FoilBoard app;
      JTextArea text;

      Data (FoilBoard target) {
        setLayout(new GridLayout(1,1,0,0));
        text = new JTextArea();
        text.setEditable(false);
        text.append("Kite/Windsurf Hydrofoil Simulator V1.0.\nDerived from FoilSim III 1.5b beta");
        add(new JScrollPane(text));
      }

      public void paint (Graphics g) {
        // System.out.println("-- paint " + this);
        genReport();
        super.paint(g);
      }

      void genReport() {
        //?? pboflag = 0;

        current_part.save_state(); 

        JTextArea out = text;

        if (!t_foil_name.equals("Test"))
          out.append("\n\nHydrofoil: " + t_foil_name);

        java.util.Date date = new java.util.Date();
        out.append(    "\n     Date: " + date);
        
        wing.print( "Main Wing", out);
        stab.print( "Stabilizer Wing", out);
        strut.print("Mast (a.k.a. Strut)", out);
        fuse.print( "Fuselage", out);

        out.append( "\n\n");
        // tail volume is LAElev * AreaElev / (MACWing * AreaWing)
        // LAElev : The elevator's Lever Arm measured at the wing's and elevator's quarter chord point
        double LAElev = stab.xpos + stab.chord_xoffs + 0.25*stab.chord  - (wing.xpos + wing.chord_xoffs + 0.25*wing.chord);
        // MAC : The main wing's Mean Aerodynamic Chord
        // AreaWing : The main wing's area
        // AreaElev : The elevator's area
        
        out.append( "\nTail Voulume: " + LAElev*stab.span*stab.chord/(wing.chord*wing.chord*wing.span));
        

        out.append( "\n\n");
        switch (planet) {
        case 0: {       
          out.append( "\n Standard Earth Atmosphere" );
          break;
        }
        case 1: {       
          out.append( "\n Martian Atmosphere" );
          break;
        }
        case 2: {       
          out.append( "\n Water" );
          break;
        }
        case 3: {       
          out.append( "\n Specified Conditions" );
          break;
        }
        case 4: {       
          out.append( "\n Specified Conditions" );
          break;
        }
        }

        // out.append( "\n Altitude = " + filter0(alt) );
        // if (lunits == IMPERIAL) out.append( " ft ," );
        // else /*METRIC*/         out.append( " m ," );
      
        switch (lunits)  {
        case 0: {                             /* English */
          out.append( "\n Density = " + filter5(rho_EN) );
          out.append( "slug/cu ft" );
          out.append( "\n Pressure = " + filter3(ps0/144.) );
          out.append( "lb/sq in," );
          out.append( " Temperature = " + filter0(ts0 - 460.) );
          out.append( "F," );
          break;
        }
        case 1: {                             /* Metric */
          out.append( " Density = " + filter3(rho_EN*515.4) );
          out.append( "kg/cu m" );
          out.append( "\n Pressure = " + filter3(101.3/14.7*ps0/144.) );
          out.append( "kPa," );
          out.append( " Temperature = " + filter0(ts0*5.0/9.0 - 273.1) );
          out.append( "C," );
          break;
        }
        }

        out.append( "\n Speed = " + filter1(velocity * (lunits==IMPERIAL? 0.868976 : 0.539957 )) + "Kts, or" );
        out.append( " " + filter1(velocity) );
        if (lunits == IMPERIAL) out.append( " mph ," );
        else /*METRIC*/         out.append( " km/hr ," );

        // if (lftout == 1)
        //   out.append( "\n  Lift Coefficient = " + filter3(current_part.cl) );
        // if (lftout == 0) {
        //   if (Math.abs(lift) <= 10.0) out.append( "\n  Lift = " + filter3(lift) );
        //   if (Math.abs(lift) > 10.0) out.append( "\n  Lift  = " + filter0(lift) );
        //   if (lunits == IMPERIAL) out.append( " lbs " );
        //   else /*METRIC*/         out.append( " Newtons " );
        // }
        // if ( polarOut == 1)
        //   out.append( "\n  Drag Coefficient = " + filter3(current_part.cd) );
        // if (lftout == 0) {
        //   out.append( "\n  Drag  = " + filter0(drag) );
        //   if (lunits == IMPERIAL) out.append( " lbs " );
        //   else /*METRIC*/         out.append( " Newtons " );
        // }

        out.append( "\n  Lift = " + con.outTotalLift.getText());
        out.append( "\n  Drag  = " + con.outTotalDrag.getText());

        if (min_takeoff_speed_info != null) 
          out.append( "\n\n" + min_takeoff_speed_info);

        if (cruising_info != null) 
          out.append( "\n\n" + cruising_info);

        if (max_speed_info != null) 
          out.append( "\n\n" + max_speed_info);
      }
    }  // Data

    class PerfWeb extends Panel {
      FoilBoard app;
      javax.swing.JEditorPane prnt;

      PerfWeb (FoilBoard target) {
        setLayout(new GridLayout(1,1,0,0));
        prnt = new javax.swing.JEditorPane();
        prnt.setAutoscrolls(true);
        prnt.setContentType("text/html");
        add(prnt);
      }

      public void paint (Graphics g) {
        // System.out.println("-- paint " + this);
        updateReport();
        super.paint(g);
      }

      String cg_text (String cg_text) { 
        if (cg_text.equals("-"))
          return "??";
        else 
          return cg_text + " mast LE";
      
      }

      void updateReport () { 
        long start = System.currentTimeMillis();
        prnt.setText(genReport()); 
        // System.out.println("-- PerfWeb updateReport, ms: " + (System.currentTimeMillis() - start));
      }
      
      String genReport () {
        String l_u = current_display_size_unit_string();
        String v_u = current_display_speed_unit_string();
        String f_u = current_display_force_unit_string();
        String html_text = 
              "<style>\n table {border:1px solid black;border-collapse:collapse;width:100%}\n"+
              " th, td {padding:0px;text-align:right;}\n</style>\n\n" + 
              "<body style='font-size:14pt'>\n" + 
              "<b>$NAME</b>\n" +  dateString() +
              // "<br>Specs" + 
              "<table border='0'>\n"+
              "<thead >\n"+
              "<tr style='background-color:yellow'>\n"+
              "<th>Part</th><th>Span<br>"+l_u+"</th><th>Chord<br>"+l_u+"</th><th>Thickness<br>averg,"+l_u+"</th>\n"+
              "<th>Angle<br>deg</th><th>Area<br>"+l_u+"2</th><th>Foil(Profile)</th></tr></thead>\n"+
              "<tbody>\n"+
              "  <tr style='background-color:#ffffff'><td>Wing</td><td>$WS</td><td>$WC</td><td>$WT</td><td>$Wa</td><td>$WA</td><td>$WF</td></tr>\n"+
              "  <tr style='background-color:#f0f0f0'><td>Stab</td><td>$SS</td><td>$SC</td><td>$ST</td><td>$Sa</td><td>$SA</td><td>$SF</td></tr>\n"+
              "  <tr style='background-color:#ffffff'><td>Fuse</td><td>$FS</td><td>$FC</td><td>$FT</td><td>$Fa</td><td>$FA</td><td>$FF</td></tr>\n"+
              "  <tr style='background-color:#f0f0f0'><td>Mast</td><td>$MS</td><td>$MC</td><td>$MT</td><td>$Ma</td><td>$MA</td><td>$MF</td></tr>\n"+
              "</tbody></table>\n"+
              "<table border='0'>\n"+
              "<thead >\n"+
              "<tr style='background-color:yellow'>\n"+
              "<th>Performance Evaluation</th><th>Speed<br>"+v_u+"</th><th>Lift<br>"+f_u+"</th><th>Drag<br>"+f_u+"</th><th>Lift & C.G.<br>Location</th></tr></thead>\n"+
              "<tbody>\n"+
              "  <tr style='background-color:#ffffff'><td>Takeoff (Min Speed)</td><td>$TKS</td><td>$TKL</td><td>$TKD</td><td>$TKX</td></tr>\n"+
              "  <tr style='background-color:#f0f0f0'><td>Cruise (Least Drag)</td><td>$CRS</td><td>$CRL</td><td>$CRD</td><td>$CRX</td></tr>\n"+
              "  <tr style='background-color:#ffffff'><td>Race (Max Speed)</td><td>$RCS</td><td>$RCL</td><td>$RCD</td><td>$RCX</td></tr>\n"+
              "</tbody></table>\n\n</body>"
              ;

            html_text = html_text.replace("$TKS", toStringOptQMFilter1(min_takeoff_speed));
            html_text = html_text.replace("$TKL", toStringOptQMFilter0(min_takeoff_lift));
            html_text = html_text.replace("$TKD", toStringOptQMFilter0(min_takeoff_drag));
            html_text = html_text.replace("$TKX", cg_text(min_takeoff_cg));
            html_text = html_text.replace("$CRS", toStringOptQMFilter1(cruising_speed));
            html_text = html_text.replace("$CRL", toStringOptQMFilter0(cruising_lift));
            html_text = html_text.replace("$CRD", toStringOptQMFilter0(cruising_drag));
            html_text = html_text.replace("$CRX", cg_text(cruising_cg));
            html_text = html_text.replace("$RCS", toStringOptQMFilter1(max_speed_speed));
            html_text = html_text.replace("$RCL", toStringOptQMFilter0(max_speed_lift));
            html_text = html_text.replace("$RCD", toStringOptQMFilter0(max_speed_drag));
            html_text = html_text.replace("$RCX", cg_text(max_speed_cg));


            html_text = html_text.replace("$NAME", t_foil_name);

            html_text = html_text.replace("$WS", ""+make_size_info_in_display_units(wing.span,false));
            html_text = html_text.replace("$WC", ""+make_size_info_in_display_units(wing.chord,false));
            html_text = html_text.replace("$WT", ""+make_size_info_in_display_units(wing.chord*wing.thickness/100,false)); // 100*ch*th/100%
            html_text = html_text.replace("$Wa", ""+filter1(wing.aoa));
            html_text = html_text.replace("$WA", ""+make_area_info_in_display_units(wing.span*wing.chord,false));
            html_text = html_text.replace("$WF", wing.foil_descr());

            html_text = html_text.replace("$SS", ""+make_size_info_in_display_units(stab.span,false));
            html_text = html_text.replace("$SC", ""+make_size_info_in_display_units(stab.chord,false));
            html_text = html_text.replace("$ST", ""+make_size_info_in_display_units(stab.chord*stab.thickness/100,false));
            html_text = html_text.replace("$Sa", ""+filter1(stab.aoa));
            html_text = html_text.replace("$SA", ""+make_area_info_in_display_units(stab.span*stab.chord,false));
            html_text = html_text.replace("$SF", stab.foil_descr());

            html_text = html_text.replace("$MS", ""+make_size_info_in_display_units(strut.span,false));
            html_text = html_text.replace("$MC", ""+make_size_info_in_display_units(strut.chord,false));
            html_text = html_text.replace("$MT", ""+make_size_info_in_display_units(strut.chord*strut.thickness/100,false));
            html_text = html_text.replace("$Ma", ""+filter1(strut.aoa));
            html_text = html_text.replace("$MA", ""+make_area_info_in_display_units(strut.span*strut.chord,false));
            html_text = html_text.replace("$MF", strut.foil_descr());

            html_text = html_text.replace("$FS", ""+make_size_info_in_display_units(fuse.span,false));
            html_text = html_text.replace("$FC", ""+make_size_info_in_display_units(fuse.chord,false));
            html_text = html_text.replace("$FT", ""+make_size_info_in_display_units(fuse.chord*fuse.thickness/100,false));
            html_text = html_text.replace("$Fa", ""+filter1(fuse.aoa));
            html_text = html_text.replace("$FA", ""+make_area_info_in_display_units(fuse.span*fuse.chord,false));
            html_text = html_text.replace("$FF", fuse.foil_descr());

            return html_text;

      }
    }

    class PerfWebSrc extends Panel {
      FoilBoard app;
      JTextArea text;

      PerfWebSrc (FoilBoard target) {
        setLayout(new GridLayout(1,1,0,0));
        text = new JTextArea();
        text.setEditable(false);
        add(new JScrollPane(text));
      }
      public void paint (Graphics g) {
        // System.out.println("-- paint " + this);
        text.setText(out.perfweb.genReport());
        super.paint(g);
      }
    }

  } // Out 

}
