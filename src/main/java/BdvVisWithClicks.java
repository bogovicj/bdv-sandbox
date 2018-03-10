import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.viewer.ViewerPanel;
import ij.IJ;
import ij.ImagePlus;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.real.FloatType;

public class BdvVisWithClicks {

	public static void main(String[] args) 
	{
		
		ImagePlus imp = IJ.openImage("/home/john/tmp/mri-stack.tif");
		System.out.println(imp);
		Img<FloatType> img = ImageJFunctions.convertFloat(imp);

		Bdv bdv = BdvFunctions.show( img, "mystack");
		bdv.getBdvHandle().getSetupAssignments().getMinMaxGroups().get(0).setRange( 0, 255 );

		ViewerPanel vp = bdv.getBdvHandle().getViewerPanel();
		AffineTransform3D viewerTransform = new AffineTransform3D();
		viewerTransform.set(
				1.0, 0.0, 0.0, 0.0,
				0.0, 1.0, 1.0, 0.0,
				0.0, 0.0, 2.2, 0.0 );
		vp.setCurrentViewerTransform(viewerTransform); // if this is missing, you get a NullPointer

		// this lets the viewer panel know about itself
		MouseLandmarkListener ml = new MouseLandmarkListener( vp );
	}

	public static class MouseLandmarkListener implements MouseListener
	{

		ViewerPanel vp;
		RealPoint pt;

		public MouseLandmarkListener( final ViewerPanel vp )
		{
			setViewer( vp );
			pt = new RealPoint( 3 );
			vp.getDisplay().addHandler(this);
		}

		protected void setViewer( final ViewerPanel vp )
		{
			this.vp = vp;
		}

		@Override
		public void mouseClicked( final MouseEvent arg0 )
		{
			//vp.getGlobalMouseCoordinates( pt );
			//System.out.println( "clicked global point : " + pt );
		}

		@Override
		public void mouseEntered( final MouseEvent arg0 )
		{}

		@Override
		public void mouseExited( final MouseEvent arg0 )
		{}

		@Override
		public void mousePressed( final MouseEvent e )
		{ 
			//vp.getGlobalMouseCoordinates( pt );
			//System.out.println( "pressed global point : " + pt );
		}

		@Override
		public void mouseReleased( final MouseEvent e )
		{
			vp.getGlobalMouseCoordinates( pt );
			System.out.println( "released global point : " + pt );
		}

	}
}
