package guitarjava.input;

import com.centralnexus.input.Joystick;
import com.centralnexus.input.JoystickListener;
import guitarjava.graphics.Graphics2DContext;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An Input context.
 * @author brunojadami
 */
public class InputContext implements InputInterface, KeyListener, JoystickListener, Runnable
{
    private static final int POLL_TIME = 50; // Poll time

    private List listeners; // Listeners for the input event
    private Joystick joystick; // USB Joystick
    private Thread thread; // Thread to pool input

    /**
     * Constructor.
     */
    public InputContext()
    {
        listeners = new ArrayList();
        joystick = null;
        thread = new Thread(this);
    }

    /**
     * Initializing the context.
     */
    public void init()
    {
        if (Graphics2DContext.component != null)
        {
            Graphics2DContext.component.addKeyListener(this);
        }
        else
            throw new RuntimeException("Could not find any Graphic context to add Input listeners.");
        thread.start();
    }

    /**
     * Stops the context.
     */
    public void stop()
    {
        thread.interrupt();
    }

    /**
     * Adds an Input event listener.
     * @param listener the listener
     */
    public void addInputEventListener(InputListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes an Input event listener.
     * @param listener the listener
     */
    public void removeInputEventListener(InputListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * A key was typed, from the KeyListener implementation.
     * @param e the KeyEvent
     */
    public void keyTyped(KeyEvent e)
    {
    }

    /**
     * A key was pressed, from the KeyListener implementation.
     * @param e the KeyEvent
     */
    public void keyPressed(KeyEvent e)
    {
        InputEvent event = new InputEvent(this, e.getKeyCode(), InputEvent.INPUT_PRESSED);
        fireInputEvent(event);
    }

    /**
     * A key was released, from the KeyListener implementation.
     * @param e the KeyEvent
     */
    public void keyReleased(KeyEvent e)
    {
        InputEvent event = new InputEvent(this, e.getKeyCode(), InputEvent.INPUT_RELEASED);
        fireInputEvent(event);
    }

    /**
     * Fires the Input event, calling all the listeners.
     */
    private void fireInputEvent(InputEvent event)
    {
        Iterator i = listeners.iterator();
        while (i.hasNext())
        {
            ((InputListener) i.next()).inputEvent(event);
        }
    }

    /**
     * Axis changed event.
     * @param j the event object
     */
    public void joystickAxisChanged(Joystick j)
    {
        
    }

    /**
     * Button changed event.
     * @param j the event object
     */
    public void joystickButtonChanged(Joystick j)
    {
        InputEvent event = new InputEvent(this, j.getButtons(), InputEvent.INPUT_JOYSTICK);
        fireInputEvent(event);
    }

    /**
     * Run method for pooling and finding a joystick.
     */
    public void run()
    {
        boolean quit = false; // Quit flag
        Thread pollThread = null; // Thread to poll
        PollRunner pollRunner = null; // Poll runner
        while (!quit)
        {
            //TODO: Fix the joystick.poll() blocking when removes the joystick
            try
            {
                if (joystick == null)
                {
                    try
                    {
                        joystick = Joystick.createInstance();
                        if (joystick != null)
                        {
                            joystick.setPollInterval(POLL_TIME);
                            joystick.addJoystickListener(this);
                        }
                    }
                    catch (IOException ex)
                    {
                        joystick = null;
                    }
                    if (joystick == null)
                        quit = true;
                }
                else
                {
                    if (pollThread == null || pollRunner.done)
                    {
                        pollRunner = new PollRunner(joystick);
                        pollThread = new Thread(pollRunner);
                        pollThread.start();
                    }
                    else
                    {
                        pollThread.interrupt();
                        quit = true;
                    }
                }
                Thread.sleep(POLL_TIME);
            }
            catch (InterruptedException ex)
            {
            }
        }
    }

}

/**
 * Adding a fix to the poll block bug.
 * @author brunojadami
 */
class PollRunner implements Runnable
{
    private Joystick joystick; // Joystick object
    protected boolean done; // Done polling

    public PollRunner(Joystick joystick)
    {
        this.joystick = joystick;
        done = false;
    }
    /**
     * Run method.
     */
    public void run()
    {
        joystick.poll();
        done = true;
    }

}
