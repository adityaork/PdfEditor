 

import com.sun.pdfview.Flag;
import com.sun.pdfview.FullScreenWindow;
import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFDestination;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPrintPage;
import com.sun.pdfview.PageChangeListener;
import com.sun.pdfview.PagePanel;
import com.sun.pdfview.ThumbPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;

import com.sun.pdfview.action.GoToAction;
import com.sun.pdfview.action.PDFAction;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectForm;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLine;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationRubberStamp;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationSquareCircle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.util.MapUtil;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

        /**
         @author aditya
         *PDF Editor and viewer
         *
         */
        public class PDFEditor extends JFrame implements  KeyListener,
                TreeSelectionListener, PageChangeListener {

            public final static String TITLE = "Ork PDF Viewer";

            /* XML task variables */
            String branch,year,examiner,subcode,xcomment,xhigh_text,regno;
            int numtick,numcross,numcircle,numques_mark,queschecked;
            int ques_fm,ques_m;
            int mc=0;
            DocumentBuilderFactory dfactory;
            DocumentBuilder docbuilder;
            Document doc;
            TransformerFactory tfactory;
            Transformer transformer;
            DOMSource source;
            StreamResult result;
            Element class_n,student_n,sub_n;
            Qcm qcm;
            Runtime runtime;
            /** The current PDFFile */
            PDFFile curFile;
            /** the name of the current document */
            String docName;
            /** The split between thumbs and page */
            JSplitPane split;
            /** The thumbnail scroll pane */
            JScrollPane thumbscroll;
            JScrollPane panescroll;
            /** The thumbnail display */
            ThumbPanel thumbs;
            /** The page display */
            PagePanel page;
            /** The full screen page display, or null if not in full screen mode */
            PagePanel fspp;


            //    Thread anim;
            /** The current page number (starts at 0), or -1 if no page */
            int curpage = -1,curques;
            /** the full screen button */
            JToggleButton fullScreenButton;

            //variables of pdfbox
            //start
            File filep;
            private static final String SAVE_GRAPHICS_STATE = "q\n";
            private static final String RESTORE_GRAPHICS_STATE = "Q\n";
            private static final String CONCATENATE_MATRIX = "cm\n";
            private static final String XOBJECT_DO = "Do\n";
            private static final String SPACE = " ";
            private static NumberFormat formatDecimal = NumberFormat.getNumberInstance(Locale.US);
            int px,py;
            int ph,pwd,vx,vy,pgh,pgw,x,y,ax,bx,x1,y1,fx,fy;
            float xc,yc,pagec;
            /**starting and endpoint on pdf page and on screen on mouse drag*/
            Point st,end,stons,endons;
            boolean dragflag=true;
            boolean acroflag=false;
            Process process;
            //end
            /**Different buttons for different actions*/
            String sword;
            /**Command index for syncronization*/
            int cmdindex;
            JButton comment;
            ImageIcon ic_comment;
            JButton highalit;
            JButton tick;
            JButton cross;
            JButton ques;
            JButton circle;
            JButton adobe;
            JTextField textserch;
            JTextField currquesbx;
            JLabel textslbl;
            JLabel cques;
            JButton wordlit;
            JButton delant;
            JButton marks_b;
            /** the current page number text field */
            JTextField pageField;
            /** the full screen window, or null if not in full screen mode */
            FullScreenWindow fullScreen;
            /** the root of the outline, or null if there is no outline */
            OutlineNode outline = null;
            /** The page format for printing */
            PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
            /** true if the thumb panel should exist at all */
            boolean doThumb = true;
            /** flag to indicate when a newly added document has been announced */
            Flag docWaiter;
            /** a thread that pre-loads the next page for faster response */
            PagePreparer pagePrep;
            /** the window containing the pdf outline, or null if one doesn't exist */
            JDialog olf;
            /** the document menu */
            JMenu docMenu;
            /// FILE MENU

            /**Defining actions for performing different tasks*/
            Action openAction = new AbstractAction("Open...") {

                public void actionPerformed(ActionEvent evt) {
                    doOpen();
                }
            };
            Action pageSetupAction = new AbstractAction("Page setup...") {

                public void actionPerformed(ActionEvent evt) {
                    doPageSetup();
                }
            };
            Action printAction = new AbstractAction("Print...") {

                public void actionPerformed(ActionEvent evt) {
                    doPrint();
                }
            };
            Action closeAction = new AbstractAction("Close") {

                public void actionPerformed(ActionEvent evt) {
                    doClose();
                }
            };
            Action quitAction = new AbstractAction("Quit") {

                public void actionPerformed(ActionEvent evt) {
                    doQuit();
                }
            };

            class ZoomAction extends AbstractAction {

                double zoomfactor = 1.0;

                public ZoomAction(String name, double factor) {
                    super (name);
                    zoomfactor = factor;
                }

                public ZoomAction(String name, Icon icon, double factor) {
                    super (name, icon);
                    zoomfactor = factor;
                }

                public void actionPerformed(ActionEvent evt) {
                    doZoom(zoomfactor);
                }
            }

            ZoomAction zoomInAction = new ZoomAction("Zoom in", 2.0);
            ZoomAction zoomOutAction = new ZoomAction("Zoom out",0.5);
            Action zoomToolAction = new AbstractAction("") {

                public void actionPerformed(ActionEvent evt) {
                    doZoomTool();
                }
            };
            Action fitInWindowAction = new AbstractAction("Fit in window") {

                public void actionPerformed(ActionEvent evt) {
                    doFitInWindow();
                }
            };

            class ThumbAction extends AbstractAction implements
                    PropertyChangeListener {

                boolean isOpen = true;

                public ThumbAction() {
                    super ("Hide thumbnails");
                }

                public void propertyChange(PropertyChangeEvent evt) {
                    int v = ((Integer) evt.getNewValue()).intValue();
                    if (v <= 1) {
                        isOpen = false;
                        putValue(ACTION_COMMAND_KEY, "Show thumbnails");
                        putValue(NAME, "Show thumbnails");
                    } else {
                        isOpen = true;
                        putValue(ACTION_COMMAND_KEY, "Hide thumbnails");
                        putValue(NAME, "Hide thumbnails");
                    }
                }

                public void actionPerformed(ActionEvent evt) {
                    doThumbs(!isOpen);
                }
            }

            ThumbAction thumbAction = new ThumbAction();
            Action fullScreenAction = new AbstractAction("Full screen") {

                public void actionPerformed(ActionEvent evt) {
                    doFullScreen((evt.getModifiers() & evt.SHIFT_MASK) != 0);
                }
            };
            Action nextAction = new AbstractAction("Next") {

                public void actionPerformed(ActionEvent evt) {
                    doNext();
                }
            };
            Action firstAction = new AbstractAction("First") {

                public void actionPerformed(ActionEvent evt) {
                    doFirst();
                }
            };
            Action lastAction = new AbstractAction("Last") {

                public void actionPerformed(ActionEvent evt) {
                    doLast();
                }
            };
            Action prevAction = new AbstractAction("Prev") {

                public void actionPerformed(ActionEvent evt) {
                    doPrev();
                }
            };
            Action cmntAction = new AbstractAction("",new ImageIcon("comment.png")) {

                public void actionPerformed(ActionEvent e) {
                     cmdindex= 1;
                     setCursor(CROSSHAIR_CURSOR);
                }
            };
            Action hlaAction = new AbstractAction("",new ImageIcon("high.png")) {

                 public void actionPerformed(ActionEvent e) {
                      cmdindex=2;
                      setCursor(Cursor.HAND_CURSOR);
                 }
             };
            Action tickAction = new AbstractAction("",new ImageIcon("tick.png")) {

                 public void actionPerformed(ActionEvent e) {
                     cmdindex=3;
                     setCursor(Cursor.HAND_CURSOR);
                 }
             };
            Action crossAction = new AbstractAction("",new ImageIcon("cross.png")) {

                 public void actionPerformed(ActionEvent e) {
                     cmdindex=4;
                     setCursor(Cursor.HAND_CURSOR);

                 }
             };
             Action quesAction = new AbstractAction("",new ImageIcon("ques.png")) {

                 public void actionPerformed(ActionEvent e) {
                     cmdindex=5;
                     setCursor(Cursor.HAND_CURSOR);

                 }
             };
             Action circleAction = new AbstractAction("",new ImageIcon("circle.png")) {

                 public void actionPerformed(ActionEvent e) {
                     cmdindex=6;
                     setCursor(Cursor.HAND_CURSOR);

                 }
             };
            Action clannot = new AbstractAction("",new ImageIcon("clear.png")) {

                 public void actionPerformed(ActionEvent e) {
                   doclannot();
                 }
             };

           Action mark_action = new AbstractAction("",new ImageIcon("marks.png")) {
                 public void actionPerformed(ActionEvent ae) {
                    domarks();
                 }
             };

             Action showacro = new AbstractAction("",new ImageIcon("acro.png")) {

                 public void actionPerformed(ActionEvent e) {
                   Runtime runtime = Runtime.getRuntime();
                   try {
                    process = runtime.exec("acroread "+filep.getPath());
                    acroflag= true;
                     }
                   catch(Exception ex) {
                       System.out.println("showacro "+ex.getMessage());
                   }

                 }
             };
            /**
             * Create a new PDFEditor based on a user, with or without a thumbnail
             * panel.
             useThumbs true if the thumb panel should exist, false if not.
             */
            public PDFEditor(boolean useThumbs,String[] args,boolean botused) {
                super (TITLE);
                addWindowListener(new WindowAdapter() {

                    public void windowClosing(WindowEvent evt) {
                        doQuit();
                    }
                });
                doThumb = useThumbs;
                if(botused) {
                examiner=args[0];
                subcode=args[1];
                branch=args[2];
                year=args[3];
                regno=args[4];
                }
                init();
                xmlinit();
                if(botused) {
                    doOpen("orks.pdf");
                }
            }

            /**
             * Initialize this PDFEditor by creating the GUI.
             */
            protected void init() {
                st=new Point();
                end=new Point();
                stons=new Point();
                endons=new Point();
                page = new PagePanel();
                page.addKeyListener(this );
                page.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {

                x=e.getX();
                y=e.getY();
                System.out.println("x and y :"+x+" "+y);
                transform(x,y);
                if(x>=ax && x<=bx) {
                    if(cmdindex==1) {
                        doComment();
                        cmdindex=0;
                    }
                }
                else if(cmdindex!=0) {
                    JOptionPane.showMessageDialog(page,"Please click inside page","Error!!",JOptionPane.ERROR_MESSAGE);
                    setCursor(DEFAULT_CURSOR);
                }

            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {
                try {
                    if(dragflag==false) {
                        int x,y;
                        x=e.getX();
                        y=e.getY();
                        endons.x =x;
                        endons.y =y;
                        end=transform(x,y);
                        dragflag=true;
                        if(stons.x>=ax && stons.x<=bx && endons.x>=ax && endons.x<=bx && stons.x!=endons.x && stons.y!=endons.y) {
                            if(cmdindex==2) {
                                doHighalit();
                                cmdindex=0;
                            }
                            else if(cmdindex == 3) {
                                dotick();
                                cmdindex=0;
                            }
                            else if(cmdindex==4) {
                                docross();
                                cmdindex=0;
                            }
                            else if(cmdindex==5) {
                                doques();
                                cmdindex=0;
                            }
                            else if(cmdindex==6) {
                                docircle();
                                cmdindex=0;
                            }
                        }
                        else if(cmdindex!=0) {
                            JOptionPane.showMessageDialog(page,"clicked outside or no area","Error!!",JOptionPane.ERROR_MESSAGE);
                            setCursor(DEFAULT_CURSOR);
                        }
                    }
                    System.out.println("released success");
                }
                catch(Exception ex) {
                    System.out.println("Relased: "+ex.getMessage());
                }
            }

            public void mouseEntered(MouseEvent e) {

                if(acroflag==true) {
                    process.destroy();
                    acroflag=false;
                }
            }

            public void mouseExited(MouseEvent e) {

            }
        });
        page.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                try {
                int x,y;
                x= e.getX();
                y= e.getY();
                System.out.println("dragg:0"+dragflag);
                if(dragflag==true) {

                   st=transform(x,y);
                   System.out.println("dragg:1"+dragflag);
                   stons.x = x;
                   stons.y = y;
                   dragflag=false;
                }
                System.out.println("dragg:"+x+" "+y);
                }
                catch(Exception ex) {
                    System.out.println("dragg: "+ex.getMessage());
                }
            }

            public void mouseMoved(MouseEvent e) {

            }
        });

        page.addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                int p=e.getWheelRotation();
                if(p>0)
                    doNext();
                else
                    doPrev();
            }
        });

                if (doThumb) {
                    split = new JSplitPane(split.HORIZONTAL_SPLIT);
                    split.addPropertyChangeListener(
                            split.DIVIDER_LOCATION_PROPERTY, thumbAction);
                    split.setOneTouchExpandable(true);
                    thumbs = new ThumbPanel(null);
                    thumbscroll = new JScrollPane(thumbs,
                            thumbscroll.VERTICAL_SCROLLBAR_ALWAYS,
                            thumbscroll.HORIZONTAL_SCROLLBAR_NEVER);
                    split.setLeftComponent(thumbscroll);
                    split.setRightComponent(page);
                    getContentPane().add(split, BorderLayout.CENTER);
                } else {
                    getContentPane().add(page, BorderLayout.CENTER);
                }

                page.setAutoscrolls(true);
                JToolBar toolbar = new JToolBar();
                toolbar.setFloatable(false);

                JButton jb;

                jb = new JButton(firstAction);
                jb.setText("");
                jb.setIcon(new ImageIcon("first.png"));
                toolbar.add(jb);
                jb = new JButton(prevAction);
                jb.setText("");
                jb.setIcon(new ImageIcon("prev.png"));
                toolbar.add(jb);
                pageField = new JTextField("-", 3);
                //	pageField.setEnabled(false);
                pageField.setMaximumSize(new Dimension(25, 20));
                pageField.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        doPageTyped();
                    }
                });
                toolbar.add(pageField);
                jb = new JButton(nextAction);
                jb.setText("");
                jb.setIcon(new ImageIcon("next.png"));
                toolbar.add(jb);
                jb = new JButton(lastAction);
                jb.setText("");
                jb.setIcon(new ImageIcon("last.png"));
                toolbar.add(jb);

                toolbar.add(Box.createHorizontalGlue());

                fullScreenButton = new JToggleButton(fullScreenAction);
                fullScreenButton.setText("");
                fullScreenButton.setIcon(new ImageIcon("full.png"));
                fullScreenButton.setBorder(null);
                toolbar.add(fullScreenButton);
                fullScreenButton.setEnabled(true);
                
                toolbar.add(Box.createHorizontalGlue());

                marks_b = new JButton(mark_action);
                marks_b.setToolTipText("Give marks to current question");
                marks_b.setBorder(null);
                toolbar.add(marks_b);
                currquesbx = new JTextField("", 3);
                //	pageField.setEnabled(false);
                currquesbx.setMaximumSize(new Dimension(30, 25));
                currquesbx.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        int j;
                        j = Integer.parseInt(currquesbx.getText());
                        if((ques_m==-1 ||ques_fm==-1 )&& mc!=0){
                            JOptionPane.showMessageDialog(page,"Please give marks","Marks",JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                       
                        if(qcm.check(j)==true && j!=0) {
                        if(mc!=0) {
                            modxml();
                        }
                        curques=j;
                        cques.setText("Curr Ques: "+curques);
                        numcross=numcircle=numques_mark=numtick=0;
                        xcomment=xhigh_text="";
                        ques_m=ques_fm=-1;
                        }
                        else {
                            JOptionPane.showMessageDialog(page,"Question already checked","Error!!",JOptionPane.INFORMATION_MESSAGE);
                        }
                         mc++;
                    }
                });
                toolbar.add(currquesbx);
                cques = new  JLabel();
                toolbar.add(cques);

                toolbar.add(Box.createHorizontalGlue());
                
                toolbar.add(Box.createHorizontalGlue());
                ic_comment = new ImageIcon("comment.png");
                comment = new JButton(cmntAction);
                comment.setToolTipText("Use this to comment at a point");
                comment.setBorder(null);
                toolbar.add(comment);
                comment.setEnabled(true);

                highalit = new JButton(hlaAction);
                highalit.setToolTipText("Highlight the selected area");
                highalit.setBorder(null);
                toolbar.add(highalit);
                highalit.setEnabled(true);

                tick = new JButton(tickAction);
                tick.setToolTipText("Tick the selected area");
                tick.setBorder(null);
                toolbar.add(tick);
                tick.setEnabled(true);

                cross = new JButton(crossAction);
                cross.setToolTipText("Cross the selected area");
                cross.setBorder(null);
                toolbar.add(cross);
                cross.setEnabled(true);

                ques = new JButton(quesAction);
                ques.setToolTipText("Put Question mark on selected area");
                ques.setBorder(null);
                toolbar.add(ques);
                ques.setEnabled(true);

                circle = new JButton(circleAction);
                circle.setToolTipText("Put circle on selected area");
                circle.setBorder(null);
                toolbar.add(circle);
                circle.setEnabled(true);

                toolbar.add(Box.createHorizontalGlue());


                JToggleButton jtb;
                ButtonGroup bg = new ButtonGroup();

                toolbar.add(Box.createHorizontalGlue());

                adobe= new JButton(showacro);
                adobe.setToolTipText("View in adobe reader");
                adobe.setBorder(null);
                toolbar.add(adobe);
                adobe.setEnabled(true);

                delant= new JButton(clannot);
                delant.setToolTipText("Clear all annotations of this page");
                delant.setBorder(null);
                toolbar.add(delant);
                delant.setEnabled(true);

                jb = new JButton(printAction);
                jb.setText("");
                jb.setIcon(new ImageIcon("print.gif"));
                jb.setBorder(null);
                toolbar.add(jb);

                getContentPane().add(toolbar, BorderLayout.NORTH);

                JMenuBar mb = new JMenuBar();
                JMenu file = new JMenu("File");
                file.add(openAction);
                file.add(closeAction);
                file.addSeparator();
                file.add(pageSetupAction);
                file.add(printAction);
                file.addSeparator();
                file.add(quitAction);
                mb.add(file);
                JMenu view = new JMenu("View");
                JMenu zoom = new JMenu("Zoom");
                zoom.add(zoomInAction);
                zoom.add(zoomOutAction);
                zoom.add(fitInWindowAction);
                zoom.setEnabled(false);
                view.add(zoom);
                view.add(fullScreenAction);
                mb.setBackground(new Color(100, 100, 30));

                if (doThumb) {
                    view.addSeparator();
                    view.add(thumbAction);
                }

                mb.add(view);
                setJMenuBar(mb);
                setEnabling();
                pack();
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (screen.width - getWidth()) / 2;
                int y = (screen.height - getHeight()) / 2;
                setLocation(x, y);
                if (SwingUtilities.isEventDispatchThread()) {
                    show();
                } else {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {

                            public void run() {
                                show();
                            }
                        });
                    } catch (InvocationTargetException ie) {
                        // ignore
                    } catch (InterruptedException ie) {
                        // ignore
                    }
                }
            }

            public void xmlinit() {
                try {
                Scanner sc = new Scanner(new File("orks.qcm"));
                int t = sc.nextInt();
                sc.close();
                qcm= new Qcm();
                qcm.setnum(t);

                dfactory = DocumentBuilderFactory.newInstance();
                docbuilder = dfactory.newDocumentBuilder();
                doc = docbuilder.newDocument();

                class_n = doc.createElement("class");
                class_n.setAttribute("branch", branch);
                class_n.setAttribute("year",year);
                doc.appendChild(class_n);
                student_n = doc.createElement("student");
                student_n.setAttribute("regno",regno);
                class_n.appendChild(student_n);
                sub_n = doc.createElement("subject");
                sub_n.setAttribute("code",subcode);
                student_n.appendChild(sub_n);
                }
                catch(Exception e) {
                    System.out.println("xmlinit: "+e.getMessage());
                }

                
            }
            public void modxml() {
                try {
                qcm.set(curques);
                Element ques_n = doc.createElement("question");
                ques_n.setAttribute("ques_no",""+curques);
                Element tick_n = doc.createElement("tick");
                Element cross_n =doc.createElement("cross");
                Element qm_n =doc.createElement("ques_mark");
                Element circle_n =doc.createElement("circle");
                Element ht_n =doc.createElement("high_text");
                Element cmnt_n =doc.createElement("comment");
                Element marks_n =doc.createElement("marks");
                Element fmarks_n =doc.createElement("full_marks");
                Element chk_n =doc.createElement("checked_by");
                sub_n.appendChild(ques_n);
                tick_n.setTextContent(""+numtick);
                ques_n.appendChild(tick_n);
                cross_n.setTextContent(""+numcross);
                ques_n.appendChild(cross_n);
                qm_n.setTextContent(""+numques_mark);
                ques_n.appendChild(qm_n);
                circle_n.setTextContent(""+numcircle);
                ques_n.appendChild(circle_n);
                ht_n.setTextContent(xhigh_text);
                ques_n.appendChild(ht_n);
                cmnt_n.setTextContent(xcomment);
                ques_n.appendChild(cmnt_n);
                marks_n.setTextContent(""+ques_m);
                ques_n.appendChild(marks_n);
                fmarks_n.setTextContent(""+ques_fm);
                ques_n.appendChild(fmarks_n);
                chk_n.setTextContent(examiner);
                ques_n.appendChild(chk_n);
                }
                catch(Exception e) {
                    System.out.println("modxml: "+e.getMessage());
                }

            }
            /**
             * Changes the displayed page, desyncing if we're not on the
             * same page as a presenter.
             */
            public void gotoPage(int pagenum) {
                if (pagenum < 0) {
                    pagenum = 0;
                } else if (pagenum >= curFile.getNumPages()) {
                    pagenum = curFile.getNumPages() - 1;
                }
                forceGotoPage(pagenum);
            }

            /**
             * Changes the displayed page.
             */
            public void forceGotoPage(int pagenum) {
                if (pagenum <= 0) {
                    pagenum = 0;
                } else if (pagenum >= curFile.getNumPages()) {
                    pagenum = curFile.getNumPages() - 1;
                }
                //        System.out.println("Going to page " + pagenum);
                curpage = pagenum;

                // update the page text field
                pageField.setText(String.valueOf(curpage + 1));

                // fetch the page and show it in the appropriate place
                PDFPage pg = curFile.getPage(pagenum + 1);
                if (fspp != null) {
                    fspp.showPage(pg);
                    fspp.requestFocus();
                } else {
                    page.showPage(pg);
                    page.requestFocus();
                }

                // update the thumb panel
                if (doThumb) {
                    thumbs.pageShown(pagenum);
                }

                // stop any previous page prepper, and start a new one
                if (pagePrep != null) {
                    pagePrep.quit();
                }
                pagePrep = new PagePreparer(pagenum);
                pagePrep.start();

                setEnabling();
                }

            /**
             * A class to pre-cache the next page for better UI response
             */
            class PagePreparer extends Thread {

                int waitforPage;
                int prepPage;

                /**
                 * Creates a new PagePreparer to prepare the page after the current
                 * one.
                 * @param waitforPage the current page number, 0 based
                 */
                public PagePreparer(int waitforPage) {
                    setDaemon(true);

                    this .waitforPage = waitforPage;
                    this .prepPage = waitforPage + 1;
                }

                public void quit() {
                    waitforPage = -1;
                }

                public void run() {
                    Dimension size = null;
                    Rectangle2D clip = null;

                    // wait for the current page
                    //            System.out.println("Preparer waiting for page " + (waitforPage + 1));
                    if (fspp != null) {
                        fspp.waitForCurrentPage();
                        size = fspp.getCurSize();
                        clip = fspp.getCurClip();
                    } else if (page != null) {
                        page.waitForCurrentPage();
                        size = page.getCurSize();
                        clip = page.getCurClip();
                    }

                    if (waitforPage == curpage) {
                        // don't go any further if the user changed pages.
                        //                System.out.println("Preparer generating page " + (prepPage + 2));
                        PDFPage pdfPage = curFile.getPage(prepPage + 1, true);
                        if (pdfPage != null && waitforPage == curpage) {
                            // don't go any further if the user changed pages
                            //                    System.out.println("Generating image for page " + (prepPage + 2));

                            pdfPage.getImage(size.width, size.height, clip,
                                    null, true, true);
                            //		    System.out.println("Generated image for page "+ (prepPage+2));
                        }
                    }
                }
            }

            /**
             *Function that transform onscreen point to point on pdfpage
             * @param x
             * @param y
             * @return
             */
            public Point transform(int x,int y) {
                try {
                    Point p= new Point();
                    pwd=page.getWidth();
                    ph=page.getHeight();
                    //System.out.println("Panel: "+pwd+" "+ph);
                    pgw=(int)page.getPage().getWidth();
                    pgh=(int)page.getPage().getHeight();
                   // System.out.println("Page: "+pgh+" "+pgw);
                    pagec=(float)(1.0*pgh/pgw);
                    //System.out.println("Pagec: "+pagec);
                    vy=ph;
                    vx=(int)(vy/pagec);
                   // System.out.println("Virtual: "+vx+" "+vy);
                    ax=(pwd-vx)/2;
                    bx=(pwd+vx)/2;
                   // System.out.println("Boundary: "+ax+" "+bx);
                    xc=(float)(1.0*pgw/vx);
                    yc=(float)(1.0*pgh/vy);
                   // System.out.println("xc and yc: "+xc+" "+yc);
                    x1=x-ax;
                    y1=ph-y;
                   // System.out.println("Intermediate: "+x1+" "+y1);
                    fx=(int)(xc*x1);
                    fy=(int)(yc*y1);
                  //  System.out.println("final: "+fx+" "+fy);
                    p.x=fx;
                    p.y=fy;
                    return p;
                }
                catch(Exception e) {
                    System.out.println("transform: "+e.getMessage());
                    return null;
                }
            }
            /**
             * Enable or disable all of the actions based on the current state.
             */
            public void setEnabling() {
                boolean fileavailable = curFile != null;
                boolean pageshown = ((fspp != null) ? fspp.getPage() != null
                        : page.getPage() != null);
                boolean printable = fileavailable && curFile.isPrintable();

                pageField.setEnabled(fileavailable);
                printAction.setEnabled(printable);
                closeAction.setEnabled(fileavailable);
                fullScreenAction.setEnabled(pageshown);
                prevAction.setEnabled(pageshown);
                nextAction.setEnabled(pageshown);
                firstAction.setEnabled(fileavailable);
                lastAction.setEnabled(fileavailable);
                zoomToolAction.setEnabled(pageshown);
                fitInWindowAction.setEnabled(pageshown);
                zoomInAction.setEnabled(pageshown);
                zoomOutAction.setEnabled(pageshown);
            }

            /**
             * Open a specific pdf file.  Creates a DocumentInfo from the file,
             * and opens that.
             * @param file the file to open
             */
            public void openFile(File file) throws IOException {
                // first open the file for random access
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                filep=file;
                // extract a file channel
                FileChannel channel = raf.getChannel();

                // now memory-map a byte-buffer
                ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                        channel.size());

                // create a PDFFile from the data
                PDFFile newfile = null;
                try {
                    newfile = new PDFFile(buf);
                } catch (IOException ioe) {
                    openError(file.getPath()
                            + " doesn't appear to be a PDF file.");
                    return;
                }

                // Now that we're reasonably sure this document is real, close the
                // old one.
                doClose();

                // set up our document
                this .curFile = newfile;
                docName = file.getName();
                setTitle(TITLE + ": " + docName);

                // set up the thumbnails
                if (doThumb) {
                    thumbs = new ThumbPanel(curFile);
                    thumbs.addPageChangeListener(this );
                    thumbscroll.getViewport().setView(thumbs);
                    thumbscroll.getViewport().setBackground(Color.gray);
                }

                setEnabling();

                // display page 1.
                forceGotoPage(0);

                // if the PDF has an outline, display it.
                try {
                    outline = curFile.getOutline();
                } catch (IOException ioe) {
                }
                if (outline != null) {
                    if (outline.getChildCount() > 0) {
                        olf = new JDialog(this , "Outline");
                        olf
                                .setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        olf.setLocation(this .getLocation());
                        JTree jt = new JTree(outline);
                        jt.setRootVisible(false);
                        jt.addTreeSelectionListener(this );
                        JScrollPane jsp = new JScrollPane(jt);
                        olf.getContentPane().add(jsp);
                        olf.pack();
                        olf.setVisible(true);
                    } else {
                        if (olf != null) {
                            olf.setVisible(false);
                            olf = null;
                        }
                    }
                }
            }

            /**
             * Display a dialog indicating an error.
             */
            public void openError(String message) {
                JOptionPane.showMessageDialog(split, message,
                        "Error opening file", JOptionPane.ERROR_MESSAGE);
            }

            /**
             * A file filter for PDF files.
             */
            FileFilter pdfFilter = new FileFilter() {

                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".pdf");
                }

                public String getDescription() {
                    return "Choose a PDF file";
                }
            };
            private File prevDirChoice;

            /*
             * marking
             */
            public void domarks() {
                ques_m=Integer.parseInt(JOptionPane.showInputDialog("Enter the marks"));
                ques_fm=Integer.parseInt(JOptionPane.showInputDialog("Enter the Full marks"));

            }
            /**
             * Ask the user for a PDF file to open from the local file system
             */
            /**
             * delete all annotations of the current page
             */
            public void doclannot() {
                try {
                    PDDocument document = PDDocument.load(filep.getPath());
                    List list = document.getDocumentCatalog().getAllPages();
                    PDPage ppage = (PDPage)list.get(curpage);
                    List la = new ArrayList();
                    ppage.setAnnotations(la);
                    document.save(filep.getPath());
                    JOptionPane.showMessageDialog(page,"All annotations deleted","Clear",JOptionPane.INFORMATION_MESSAGE);
                }
                catch(Exception e) {
                    System.out.println("doclannot: "+e.getMessage());
                }
            }
            /**
             * fucntion that perform commenting
             */
            public void doComment() {
                Point mp;
                String note =JOptionPane.showInputDialog("Enter the note");

                PDDocument document = null;
                try {
                System.out.println("Add:"+filep.getPath());
                document=PDDocument.load(filep.getPath());
                System.out.println("Loaded successfully");

                List list = new ArrayList();
                 System.out.println("Paged :0 successfully");
               list = document.getDocumentCatalog().getAllPages();
                //document.getDocumentCatalog().getPages().getAllKids(list);
                System.out.println("Paged :1 successfully");
                PDPage ppage = (PDPage)list.get(curpage);

                System.out.println("Paged:2 successfully");

                List annotations = ppage.getAnnotations();
                PDAnnotationRubberStamp rubberStamp = new PDAnnotationRubberStamp();
                rubberStamp.setName(PDAnnotationRubberStamp.NAME_TOP_SECRET);
                rubberStamp.setRectangle(new PDRectangle(100,100));

                rubberStamp.setContents(note);

                // Create a PDXObjectImage with the given jpg
                FileInputStream fin = new FileInputStream("comment.jpg");
                PDJpeg mypic = new PDJpeg(document, fin);

                //Define and set the target rectangle
                if((fx+25)>pgw) {
                    fx=pgw-25;
                }
                if((fy+25)>pgh) {
                    fy=pgh-25;
                }
                PDRectangle myrect = new PDRectangle();
                myrect.setUpperRightX(fx+20);
                myrect.setUpperRightY(fy+20);
                myrect.setLowerLeftX(fx);
                myrect.setLowerLeftY(fy);

                // Create a PDXObjectForm
                PDStream formstream = new PDStream(document);
                OutputStream os = formstream.createOutputStream();
                PDXObjectForm form = new PDXObjectForm(formstream);
                form.setResources(new PDResources());
                form.setBBox(myrect);
                form.setFormType(1);
                // adjust the image to the target rectangle and add it to the stream
                drawXObject(mypic, form.getResources(), os, fx,fy, 20, 20);
                os.close();

                PDAppearanceStream myDic = new PDAppearanceStream(
                        form.getCOSStream());
                PDAppearanceDictionary appearance = new PDAppearanceDictionary(
                        new COSDictionary());
                appearance.setNormalAppearance(myDic);
                rubberStamp.setAppearance(appearance);
                rubberStamp.setRectangle(myrect);

                //Add the new RubberStamp to the document
                annotations.add(rubberStamp);
                System.out.println("Good!");
                document.save(filep.getPath());
                document.close();
                xcomment=xcomment+"||"+note;
                setCursor(DEFAULT_CURSOR);
                }
                catch(Exception e) {
                    System.out.println("Error in doComment::"+e.getMessage());
                }

            }
            /**
             * function that draw the jpeg image on pdf page
             * @param xobject
             * @param resources
             * @param os
             * @param x
             * @param y
             * @param width
             * @param height
             * @throws IOException
             */
            private void drawXObject(PDXObjectImage xobject,
                    PDResources resources, OutputStream os, float x, float y,
                    float width, float height) throws IOException {
                // This is similar to PDPageContentStream.drawXObject()
                String xObjectPrefix = "Im";
                String objMapping = MapUtil.getNextUniqueKey(resources
                        .getImages(), xObjectPrefix);
                resources.getXObjects().put(objMapping, xobject);

                appendRawCommands(os, SAVE_GRAPHICS_STATE);
                appendRawCommands(os, formatDecimal.format(width));
                appendRawCommands(os, SPACE);
                appendRawCommands(os, formatDecimal.format(0));
                appendRawCommands(os, SPACE);
                appendRawCommands(os, formatDecimal.format(0));
                appendRawCommands(os, SPACE);
                appendRawCommands(os, formatDecimal.format(height));
                appendRawCommands(os, SPACE);
                appendRawCommands(os, formatDecimal.format(x));
                appendRawCommands(os, SPACE);
                appendRawCommands(os, formatDecimal.format(y));
                appendRawCommands(os, SPACE);
                appendRawCommands(os, CONCATENATE_MATRIX);
                appendRawCommands(os, SPACE);
                appendRawCommands(os, "/");
                appendRawCommands(os, objMapping);
                appendRawCommands(os, SPACE);
                appendRawCommands(os, XOBJECT_DO);
                appendRawCommands(os, SPACE);
                appendRawCommands(os, RESTORE_GRAPHICS_STATE);
            }

            private void appendRawCommands(OutputStream os, String commands)
                    throws IOException {
                os.write(commands.getBytes("ISO-8859-1"));
            }

            /**
             * function to highlight the selected area
             */
            public void doHighalit() {;
                int x,y;
                String text;
                Graphics2D g2 = (Graphics2D)(page.getGraphics());
                PDRectangle pos = new PDRectangle();

                if(stons.x > endons.x) {
                    x=endons.x;
                    pos.setLowerLeftX(end.x);
                    pos.setUpperRightX(st.x);
                }
                else {
                    x=stons.x;
                    pos.setLowerLeftX(st.x);
                    pos.setUpperRightX(end.x);
                }
                if(stons.y > endons.y) {
                    y=endons.y;
                    pos.setLowerLeftY(st.y);
                    pos.setUpperRightY(end.y);
                }
                else {
                    y=stons.y;
                    pos.setLowerLeftY(end.y);
                    pos.setUpperRightY(st.y);
                }
                Rectangle2D rect = new Rectangle2D.Float(x, y,abs(stons.x-endons.x), abs(stons.y-endons.y));
                System.out.println("dolight");
                g2.draw(rect);
                PDDocument document = null;
                try {
                    System.out.println("High Add:"+filep.getPath());
                    document=PDDocument.load(filep.getPath());
                    System.out.println("high Loaded successfully");
                    List list = new ArrayList();
                    System.out.println("high Paged :0 successfully");
                    list = document.getDocumentCatalog().getAllPages();
                    //document.getDocumentCatalog().getPages().getAllKids(list);
                    System.out.println("high Paged :1 successfully");
                    PDPage ppage = (PDPage)list.get(curpage);

                    System.out.println("high Paged:2 successfully");

                    List annotations = ppage.getAnnotations();
                    PDGamma colors = new PDGamma();
                    colors.setG(255);
                    colors.setB(80);
                    //colors.setR(1);
                    PDAnnotationSquareCircle rec = new PDAnnotationSquareCircle(PDAnnotationSquareCircle.SUB_TYPE_SQUARE);
                    rec.setInteriorColour(colors);
                    rec.setColour(colors);
                    rec.setBorderStyle(null);
                    rec.setRectangle(pos);
                    rec.setConstantOpacity(0.4f);
                    annotations.add(rec);
                    document.save(filep.getPath());
                    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                    stripper.setSortByPosition(true);
                  Rectangle rects = new Rectangle((int)pos.getLowerLeftX(),(int)pos.getUpperRightY(),(int)pos.getWidth(),(int)pos.getHeight());
                    stripper.addRegion("rrr", rects);
                    stripper.extractRegions(ppage);
                    xhigh_text=xhigh_text+"||"+stripper.getTextForRegion("rrr");
                    document.close();
                    setCursor(DEFAULT_CURSOR);
                }
                catch(Exception e) {
                    System.out.println("doHighalit: "+e.getMessage());
                }

            }

            /**
             * to create tick mark at specific location
             */
            public void dotick() {
                int x,y,lx,ly,w,h;
                Graphics2D g2 = (Graphics2D)(page.getGraphics());
                PDRectangle pos = new PDRectangle();
                if(stons.x > endons.x) {
                    x=endons.x;
                    pos.setLowerLeftX(end.x);
                    pos.setUpperRightX(st.x);
                }
                else {
                    x=stons.x;
                    pos.setLowerLeftX(st.x);
                    pos.setUpperRightX(end.x);
                }
                if(stons.y > endons.y) {
                    y=endons.y;
                    pos.setLowerLeftY(st.y);
                    pos.setUpperRightY(end.y);
                }
                else {
                    y=stons.y;
                    pos.setLowerLeftY(end.y);
                    pos.setUpperRightY(st.y);
                }
                Rectangle2D rect = new Rectangle2D.Float(x, y,abs(stons.x-endons.x), abs(stons.y-endons.y));
                System.out.println("dolight");
                g2.draw(rect);
                PDDocument document = null;
                PDBorderStyleDictionary bd = new PDBorderStyleDictionary();

                lx=(int)pos.getLowerLeftX();
                ly=(int)pos.getLowerLeftY();
                w=(int)pos.getUpperRightX()-lx;
                h=(int)pos.getUpperRightY()-ly;
                try {
                    document=PDDocument.load(filep.getPath());
                    List list = new ArrayList();
                    list = document.getDocumentCatalog().getAllPages();
                    //document.getDocumentCatalog().getPages().getAllKids(list);
                    PDPage ppage = (PDPage)list.get(curpage);
                    List annotations = ppage.getAnnotations();
                    bd.setWidth(3);
                    PDGamma colors = new PDGamma();
                    colors.setR(255);
                    PDAnnotationLine aline = new PDAnnotationLine();
                    PDAnnotationLine bline = new PDAnnotationLine();

                    aline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    aline.setBorderStyle(bd);
                    aline.setColour(colors);
                    aline.setRectangle(pos);
                    float[] alinepos = new float[4];
                    alinepos[0]=pos.getLowerLeftX();
                    alinepos[1]=(pos.getLowerLeftY()+pos.getUpperRightY())/2;
                    alinepos[2]=(3*pos.getLowerLeftX()+pos.getUpperRightX())/4;
                    alinepos[3]=pos.getLowerLeftY();
                    aline.setLine(alinepos);
                    aline.setConstantOpacity(0.65f);

                    bline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    bline.setBorderStyle(bd);
                    bline.setColour(colors);
                    bline.setRectangle(pos);
                    float[] blinepos = new float[4];
                    blinepos[0]=(3*pos.getLowerLeftX()+pos.getUpperRightX())/4;
                    blinepos[1]=pos.getLowerLeftY();
                    blinepos[2]=pos.getUpperRightX();
                    blinepos[3]=pos.getUpperRightY();
                    bline.setLine(blinepos);
                    bline.setConstantOpacity(0.65f);

                    annotations.add(aline);
                    annotations.add(bline);

                    document.save(filep.getPath());
                    document.close();
                    setCursor(DEFAULT_CURSOR);
                    numtick++;
                }
                catch(Exception e) {
                    System.out.println("dotick: "+e.getMessage());
                }

            }
            /**
             * create cross mark on selected area of page
             */
            public void docross() {
                int x,y,lx,ly,w,h;
                Graphics2D g2 = (Graphics2D)(page.getGraphics());
                PDRectangle pos = new PDRectangle();
                if(stons.x > endons.x) {
                    x=endons.x;
                    pos.setLowerLeftX(end.x);
                    pos.setUpperRightX(st.x);
                }
                else {
                    x=stons.x;
                    pos.setLowerLeftX(st.x);
                    pos.setUpperRightX(end.x);
                }
                if(stons.y > endons.y) {
                    y=endons.y;
                    pos.setLowerLeftY(st.y);
                    pos.setUpperRightY(end.y);
                }
                else {
                    y=stons.y;
                    pos.setLowerLeftY(end.y);
                    pos.setUpperRightY(st.y);
                }
                Rectangle2D rect = new Rectangle2D.Float(x, y,abs(stons.x-endons.x), abs(stons.y-endons.y));
                System.out.println("dolight");
                g2.draw(rect);
                PDDocument document = null;
                PDBorderStyleDictionary bd = new PDBorderStyleDictionary();

                lx=(int)pos.getLowerLeftX();
                ly=(int)pos.getLowerLeftY();
                w=(int)pos.getUpperRightX()-lx;
                h=(int)pos.getUpperRightY()-ly;
                try {
                    document=PDDocument.load(filep.getPath());
                    List list = new ArrayList();
                    list = document.getDocumentCatalog().getAllPages();
                    //document.getDocumentCatalog().getPages().getAllKids(list);
                    PDPage ppage = (PDPage)list.get(curpage);
                    List annotations = ppage.getAnnotations();
                    bd.setWidth(3);
                    PDGamma colors = new PDGamma();
                    colors.setR(255);
                    PDAnnotationLine aline = new PDAnnotationLine();
                    PDAnnotationLine bline = new PDAnnotationLine();

                    aline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    aline.setBorderStyle(bd);
                    aline.setColour(colors);
                    aline.setRectangle(pos);
                    float[] alinepos = new float[4];
                    alinepos[0]=pos.getLowerLeftX();
                    alinepos[1]=pos.getLowerLeftY();
                    alinepos[2]=pos.getUpperRightX();
                    alinepos[3]=pos.getUpperRightY();
                    aline.setLine(alinepos);
                    aline.setConstantOpacity(0.65f);

                    bline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    bline.setBorderStyle(bd);
                    bline.setColour(colors);
                    bline.setRectangle(pos);
                    float[] blinepos = new float[4];
                    blinepos[0]=pos.getLowerLeftX();
                    blinepos[1]=pos.getUpperRightY();
                    blinepos[2]=pos.getUpperRightX();
                    blinepos[3]=pos.getLowerLeftY();
                    bline.setLine(blinepos);
                    bline.setConstantOpacity(0.65f);

                    annotations.add(aline);
                    annotations.add(bline);

                    document.save(filep.getPath());
                    document.close();
                    setCursor(DEFAULT_CURSOR);
                    numcross++;
                }
                catch(Exception e) {
                    System.out.println("docross: "+e.getMessage());
                }
            }
            /**
             * create question mark on selected area
             */
            public void doques() {

                int x,y,lx,ly,w,h;
                Graphics2D g2 = (Graphics2D)(page.getGraphics());
                PDRectangle pos = new PDRectangle();
                if(stons.x > endons.x) {
                    x=endons.x;
                    pos.setLowerLeftX(end.x);
                    pos.setUpperRightX(st.x);
                }
                else {
                    x=stons.x;
                    pos.setLowerLeftX(st.x);
                    pos.setUpperRightX(end.x);
                }
                if(stons.y > endons.y) {
                    y=endons.y;
                    pos.setLowerLeftY(st.y);
                    pos.setUpperRightY(end.y);
                }
                else {
                    y=stons.y;
                    pos.setLowerLeftY(end.y);
                    pos.setUpperRightY(st.y);
                }
                Rectangle2D rect = new Rectangle2D.Float(x, y,abs(stons.x-endons.x), abs(stons.y-endons.y));
                System.out.println("dolight");
                g2.draw(rect);
                PDDocument document = null;
                PDBorderStyleDictionary bd = new PDBorderStyleDictionary();

                lx=(int)pos.getLowerLeftX();
                ly=(int)pos.getLowerLeftY();
                w=(int)pos.getUpperRightX()-lx;
                h=(int)pos.getUpperRightY()-ly;
                try {
                    document=PDDocument.load(filep.getPath());
                    List list = new ArrayList();
                    list = document.getDocumentCatalog().getAllPages();
                    //document.getDocumentCatalog().getPages().getAllKids(list);
                    PDPage ppage = (PDPage)list.get(curpage);
                    List annotations = ppage.getAnnotations();
                    bd.setWidth(3);
                    PDGamma colors = new PDGamma();
                    colors.setR(255);
                    PDAnnotationLine aline = new PDAnnotationLine();
                    PDAnnotationLine bline = new PDAnnotationLine();
                    PDAnnotationLine cline = new PDAnnotationLine();
                    PDAnnotationLine dline = new PDAnnotationLine();
                    PDAnnotationLine eline = new PDAnnotationLine();

                    aline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    aline.setBorderStyle(bd);
                    aline.setColour(colors);
                    aline.setRectangle(pos);
                    float[] alinepos = new float[4];
                    alinepos[0]=(pos.getLowerLeftX()+pos.getUpperRightX())/2;
                    alinepos[1]=pos.getUpperRightY();
                    alinepos[2]=pos.getUpperRightX();
                    alinepos[3]=pos.getUpperRightY();
                    aline.setLine(alinepos);
                    aline.setConstantOpacity(0.65f);

                    bline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    bline.setBorderStyle(bd);
                    bline.setColour(colors);
                    bline.setRectangle(pos);
                    float[] blinepos = new float[4];
                    blinepos[0]=pos.getUpperRightX();
                    blinepos[1]=pos.getUpperRightY();
                    blinepos[2]=pos.getUpperRightX();
                    blinepos[3]=(4*pos.getLowerLeftY()+6*pos.getUpperRightY())/10;
                    bline.setLine(blinepos);
                    bline.setConstantOpacity(0.65f);

                    cline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    cline.setBorderStyle(bd);
                    cline.setColour(colors);
                    cline.setRectangle(pos);
                    float[] clinepos = new float[4];
                    clinepos[0]=pos.getUpperRightX();
                    clinepos[1]=(4*pos.getLowerLeftY()+6*pos.getUpperRightY())/10;
                    clinepos[2]=(pos.getUpperRightX()+pos.getLowerLeftX())/2;
                    clinepos[3]=(4*pos.getLowerLeftY()+6*pos.getUpperRightY())/10;
                    cline.setLine(clinepos);
                    cline.setConstantOpacity(0.65f);

                    dline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    dline.setBorderStyle(bd);
                    dline.setColour(colors);
                    dline.setRectangle(pos);
                    float[] dlinepos = new float[4];
                    dlinepos[0]=(pos.getUpperRightX()+pos.getLowerLeftX())/2;
                    dlinepos[1]=(4*pos.getLowerLeftY()+6*pos.getUpperRightY())/10;
                    dlinepos[2]=(pos.getUpperRightX()+pos.getLowerLeftX())/2;
                    dlinepos[3]=(8*pos.getLowerLeftY()+2*pos.getUpperRightY())/10;
                    dline.setLine(dlinepos);
                    dline.setConstantOpacity(0.65f);

                    eline.setEndPointEndingStyle(PDAnnotationLine.LE_NONE);
                    eline.setBorderStyle(bd);
                    eline.setColour(colors);
                    eline.setRectangle(pos);
                    float[] elinepos = new float[4];
                    elinepos[0]=(pos.getUpperRightX()+pos.getLowerLeftX())/2;
                    elinepos[1]=(9*pos.getLowerLeftY()+pos.getUpperRightY())/10;
                    elinepos[2]=(pos.getUpperRightX()+pos.getLowerLeftX())/2;
                    elinepos[3]=pos.getLowerLeftY();
                    eline.setLine(elinepos);
                    eline.setConstantOpacity(0.65f);

                    annotations.add(aline);
                    annotations.add(bline);
                    annotations.add(cline);
                    annotations.add(dline);
                    annotations.add(eline);

                    document.save(filep.getPath());
                    document.close();
                    setCursor(DEFAULT_CURSOR);
                    numques_mark++;

                }
                catch(Exception e) {
                    System.out.println("doques: "+e.getMessage());
                }

            }
            /**
             * create circle on selected area of page
             */
            public void docircle() {
                int x,y;
                Graphics2D g2 = (Graphics2D)(page.getGraphics());
                PDRectangle pos = new PDRectangle();
                if(stons.x > endons.x) {
                    x=endons.x;
                    pos.setLowerLeftX(end.x);
                    pos.setUpperRightX(st.x);
                }
                else {
                    x=stons.x;
                    pos.setLowerLeftX(st.x);
                    pos.setUpperRightX(end.x);
                }
                if(stons.y > endons.y) {
                    y=endons.y;
                    pos.setLowerLeftY(st.y);
                    pos.setUpperRightY(end.y);
                }
                else {
                    y=stons.y;
                    pos.setLowerLeftY(end.y);
                    pos.setUpperRightY(st.y);
                }
                Rectangle2D rect = new Rectangle2D.Float(x, y,abs(stons.x-endons.x), abs(stons.y-endons.y));
                System.out.println("dolight");
                g2.draw(rect);
                PDDocument document = null;
                PDBorderStyleDictionary bd = new PDBorderStyleDictionary();
                try {
                    document=PDDocument.load(filep.getPath());
                    List list = new ArrayList();
                    list = document.getDocumentCatalog().getAllPages();
                    //document.getDocumentCatalog().getPages().getAllKids(list);
                    PDPage ppage = (PDPage)list.get(curpage);
                    List annotations = ppage.getAnnotations();
                    PDGamma colors = new PDGamma();
                    colors.setR(255);
                    bd.setWidth(3);
                    //colors.setR(1);
                    PDAnnotationSquareCircle rec = new PDAnnotationSquareCircle(PDAnnotationSquareCircle.SUB_TYPE_CIRCLE);
                    rec.setColour(colors);
                    rec.setBorderStyle(bd);
                    rec.setRectangle(pos);
                    rec.setConstantOpacity(0.65f);
                    annotations.add(rec);
                    document.save(filep.getPath());
                    document.close();
                    setCursor(DEFAULT_CURSOR);
                    numcircle++;
                }
                catch(Exception e) {
                    System.out.println("doHighalit: "+e.getMessage());
                }

            }
            public int abs(int n) {
                if(n>0) return n;
                else return -n;
            }
            public void doOpen() {
                try {
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(prevDirChoice);
                    fc.setFileFilter(pdfFilter);
                    fc.setMultiSelectionEnabled(false);
                    int returnVal = fc.showOpenDialog(this );
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            prevDirChoice = fc.getSelectedFile();
                            openFile(fc.getSelectedFile());
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(split,
                            "Opening files from your local "
                                    + "disk is not available\nfrom the "
                                    + "Java Web Start version of this "
                                    + "program.\n", "Error opening directory",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }

            /**
             * Open a local file, given a string filename
             * @param name the name of the file to open
             */
            public void doOpen(String name) {
                try {
                    openFile(new File(name));
                } catch (IOException ioe) {
                }
            }

            /**
             * Posts the Page Setup dialog
             */
            public void doPageSetup() {
                PrinterJob pjob = PrinterJob.getPrinterJob();
                pformat = pjob.pageDialog(pformat);
            }

            /**
             * A thread for printing in.
             */
            class PrintThread extends Thread {

                PDFPrintPage ptPages;
                PrinterJob ptPjob;

                public PrintThread(PDFPrintPage pages, PrinterJob pjob) {
                    ptPages = pages;
                    ptPjob = pjob;
                }

                public void run() {
                    try {
                        ptPages.show(ptPjob);
                        ptPjob.print();
                    } catch (PrinterException pe) {
                        JOptionPane.showMessageDialog(PDFEditor.this ,
                                "Printing Error: " + pe.getMessage(),
                                "Print Aborted", JOptionPane.ERROR_MESSAGE);
                    }
                    ptPages.hide();
                }
            }

            /**
             * Print the current document.
             */
            public void doPrint() {
                PrinterJob pjob = PrinterJob.getPrinterJob();
                pjob.setJobName(docName);
                Book book = new Book();
                PDFPrintPage pages = new PDFPrintPage(curFile);
                book.append(pages, pformat, curFile.getNumPages());

                pjob.setPageable(book);
                if (pjob.printDialog()) {
                    new PrintThread(pages, pjob).start();
                }
            }

            /**
             * Close the current document.
             */
            public void doClose() {
                if (thumbs != null) {
                    thumbs.stop();
                }
                if (olf != null) {
                    olf.setVisible(false);
                    olf = null;
                }
                if (doThumb) {
                    thumbs = new ThumbPanel(null);
                    thumbscroll.getViewport().setView(thumbs);
                }

                setFullScreenMode(false, false);
                page.showPage(null);
                curFile = null;
                setTitle(TITLE);
                setEnabling();
            }

            /**
             * Shuts down all known threads.  This ought to cause the JVM to quit
             * if the PDFViewer is the only application running.
             */
            public void doQuit() {
                //        if (thumbs != null) {
                //            thumbs.stop();
                //        }
                try {
                modxml();
                tfactory = TransformerFactory.newInstance();
                transformer = tfactory.newTransformer();
                source = new DOMSource(doc);
                result = new StreamResult(new File("temp.xml"));
                transformer.transform(source, result);
                File file = new File("orks.qcm");
                boolean f=file.delete();
                file =  new File("orks.qcm");
                Writer w = new BufferedWriter(new FileWriter(file));
                w.write(""+qcm.get());
                w.close();
                runtime = Runtime.getRuntime();
                process = runtime.exec("ftpup.sh "+"127.0.0.1"+" temp.xml"+" mnnit/temp.xml");
                process = runtime.exec("ftpup.sh "+"127.0.0.1"+" orks.pdf"+" mnnit/"+year+"_year/"+branch+"/"+subcode+"/"+regno+".pdf");
                process = runtime.exec("ftpup.sh "+"127.0.0.1"+" orks.qcm"+" mnnit/"+year+"_year/"+branch+"/"+subcode+"/"+regno+".qcm");
                }
                catch(Exception e) {
                    System.out.println("doquit: "+e.getMessage());
                }
                doClose();
                dispose();
                System.exit(0);
            }

            /**
             * Turns on zooming
             */
            public void doZoomTool() {
                if (fspp == null) {
                    page.useZoomTool(true);
                }
            }

            /**
             * Turns off zooming; makes the page fit in the window
             */
            public void doFitInWindow() {
                if (fspp == null) {
                    page.useZoomTool(false);
                    page.setClip(null);
                }
            }

            /**
             * Shows or hides the thumbnails by moving the split pane divider
             */
            public void doThumbs(boolean show) {
                if (show) {
                    split
                            .setDividerLocation((int) thumbs.getPreferredSize().width
                                    + (int) thumbscroll.getVerticalScrollBar()
                                            .getWidth() + 4);
                } else {
                    split.setDividerLocation(0);
                }
            }

            /**
             * Enter full screen mode
             * @param force true if the user should be prompted for a screen to
             * use in a multiple-monitor setup.  If false, the user will only be
             * prompted once.
             */
            public void doFullScreen(boolean force) {
                setFullScreenMode(fullScreen == null, force);
            }

            public void doZoom(double factor) {
            }


            /**
             * Goes to the next page
             */
            public void doNext() {
                gotoPage(curpage + 1);
            }

            /**
             * Goes to the previous page
             */
            public void doPrev() {
                gotoPage(curpage - 1);
            }

            /**
             * Goes to the first page
             */
            public void doFirst() {
                gotoPage(0);
            }

            /**
             * Goes to the last page
             */
            public void doLast() {
                gotoPage(curFile.getNumPages() - 1);
            }

            /**
             * Goes to the page that was typed in the page number text field
             */
            public void doPageTyped() {
                int pagenum = -1;
                try {
                    pagenum = Integer.parseInt(pageField.getText()) - 1;
                } catch (NumberFormatException nfe) {
                }
                if (pagenum >= curFile.getNumPages()) {
                    pagenum = curFile.getNumPages() - 1;
                }
                if (pagenum >= 0) {
                    if (pagenum != curpage) {
                        gotoPage(pagenum);
                    }
                } else {
                    pageField.setText(String.valueOf(curpage));
                }
            }

            /**
             * Runs the FullScreenMode change in another thread
             */
            class PerformFullScreenMode implements  Runnable {

                boolean force;

                public PerformFullScreenMode(boolean forcechoice) {
                    force = forcechoice;
                }

                public void run() {
                    fspp = new PagePanel();
                    fspp.setBackground(Color.black);
                    page.showPage(null);
                    fullScreen = new FullScreenWindow(fspp, force);
                    fspp.addKeyListener(PDFEditor.this );
                    gotoPage(curpage);
                    fullScreenAction.setEnabled(true);
                }
            }

            /**
             * Starts or ends full screen mode.
             * @param full true to enter full screen mode, false to leave
             * @param force true if the user should be prompted for a screen
             * to use the second time full screen mode is entered.
             */
            public void setFullScreenMode(boolean full, boolean force) {
                //	curpage= -1;
                if (full && fullScreen == null) {
                    fullScreenAction.setEnabled(false);
                    new Thread(new PerformFullScreenMode(force)).start();
                    fullScreenButton.setSelected(true);
                } else if (!full && fullScreen != null) {
                    fullScreen.close();
                    fspp = null;
                    fullScreen = null;
                    gotoPage(curpage);
                    fullScreenButton.setSelected(false);
                }
            }

            public static void main(String args[]) {
                String fileName = null;
                boolean useThumbs = true;
                // start the viewer
                PDFEditor editor;
                editor = new PDFEditor(useThumbs,null,false);
                if (fileName != null) {
                    editor.doOpen("test1.pdf");
                }
            }

            /**
             * Handle a key press for navigation
             */
            public void keyPressed(KeyEvent evt) {
                int code = evt.getKeyCode();
                if (code == evt.VK_LEFT) {
                    doPrev();
                } else if (code == evt.VK_RIGHT) {
                    doNext();
                } else if (code == evt.VK_UP) {
                    doPrev();
                } else if (code == evt.VK_DOWN) {
                    doNext();
                } else if (code == evt.VK_HOME) {
                    doFirst();
                } else if (code == evt.VK_END) {
                    doLast();
                } else if (code == evt.VK_PAGE_UP) {
                    doPrev();
                } else if (code == evt.VK_PAGE_DOWN) {
                    doNext();
                } else if (code == evt.VK_SPACE) {
                    doNext();
                } else if (code == evt.VK_ESCAPE) {
                    setFullScreenMode(false, false);
                }
            }

            class Qcm  {
                int num;
                public boolean check(int i) {
                    if(((num>>i)&1)==1) return false;
                    else return true;
                }

                public void set(int i){
                    num=num|(1<<i);
                }
                public void setnum(int i) {
                    num=i;
                }
                public int get() {
                    return num;
                }

            }

            /**
             * Combines numeric key presses to build a multi-digit page number.
             */
            class PageBuilder implements  Runnable {

                int value = 0;
                long timeout;
                Thread anim;
                static final long TIMEOUT = 500;

                /** add the digit to the page number and start the timeout thread */
                public synchronized void keyTyped(int keyval) {
                    value = value * 10 + keyval;
                    timeout = System.currentTimeMillis() + TIMEOUT;
                    if (anim == null) {
                        anim = new Thread(this );
                        anim.start();
                    }
                }

                /**
                 * waits for the timeout, and if time expires, go to the specified
                 * page number
                 */
                public void run() {
                    long now, then;
                    synchronized (this ) {
                        now = System.currentTimeMillis();
                        then = timeout;
                    }
                    while (now < then) {
                        try {
                            Thread.sleep(timeout - now);
                        } catch (InterruptedException ie) {
                        }
                        synchronized (this ) {
                            now = System.currentTimeMillis();
                            then = timeout;
                        }
                    }
                    synchronized (this ) {
                        gotoPage(value - 1);
                        anim = null;
                        value = 0;
                    }
                }
            }

            PageBuilder pb = new PageBuilder();

            public void keyReleased(KeyEvent evt) {
            }

            /**
             * gets key presses and tries to build a page if they're numeric
             */
            public void keyTyped(KeyEvent evt) {
                char key = evt.getKeyChar();
                if (key >= '0' && key <= '9') {
                    int val = key - '0';
                    pb.keyTyped(val);
                }
            }

            /**
             * Someone changed the selection of the outline tree.  Go to the new
             * page.
             */
            public void valueChanged(TreeSelectionEvent e) {
                if (e.isAddedPath()) {
                    OutlineNode node = (OutlineNode) e.getPath()
                            .getLastPathComponent();
                    if (node == null) {
                        return;
                    }

                    try {
                        PDFAction action = node.getAction();
                        if (action == null) {
                            return;
                        }

                        if (action instanceof  GoToAction) {
                            PDFDestination dest = ((GoToAction) action)
                                    .getDestination();
                            if (dest == null) {
                                return;
                            }

                            PDFObject page = dest.getPage();
                            if (page == null) {
                                return;
                            }

                            int pageNum = curFile.getPageNumber(page);
                            if (pageNum >= 0) {
                                gotoPage(pageNum);
                            }
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }


