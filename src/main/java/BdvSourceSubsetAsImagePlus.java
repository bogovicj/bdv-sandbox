import bdv.tools.brightness.ConverterSetup;
import bdv.util.BdvFunctions;
import bdv.util.BdvStackSource;
import ij.IJ;
import ij.ImagePlus;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class BdvSourceSubsetAsImagePlus {

	public static void main(String[] args) 
	{
		//new ImageJ();
	
		/*
		 * Load a source somehow 
		 * (this can be large enough to not fit in memory)
		 */
		ImagePlus imp = IJ.openImage("/home/john/tmp/mri-stack.tif");
		System.out.println(imp);
		Img<FloatType> img = ImageJFunctions.convertFloat(imp);

		/*
		 * Visualize with bigdataviewer
		 */
		BdvStackSource<FloatType> bdv = BdvFunctions.show( img, "mystack");
		bdv.getBdvHandle().getSetupAssignments().getMinMaxGroups().get(0).setRange( 0, 255 );
		
		/*
		 * Details on the subset of the data we want to look at
		 */
		int sourceIndex = 0; 	// which source?
		int t = 0;				// timepoint
		int level = 0;  		// mipmap level
		long[] mini = new long[]{  45,  50, 15 };
		long[] maxi = new long[]{ 124, 149, 24 };
		FinalInterval interval = new FinalInterval( mini, maxi );
		
		/*
		 * Get the subset
		 */
		RandomAccessibleInterval<FloatType> rai = Views.interval(
				bdv.getSources().get( sourceIndex ).getSpimSource().getSource(t, level ),
				interval );
		
		/*
		 * Get the display min/max values for visualization
		 * Note that getting the 'sourceIndex'th converter setups works
		 * in this case, but may not be reliable in general
		 */
		ConverterSetup convSetup = bdv.getBdvHandle().getSetupAssignments().getConverterSetups().get( sourceIndex );
		double displayMin = convSetup.getDisplayRangeMin();
		double displayMax = convSetup.getDisplayRangeMax();
		
		/*
		 * Wrap the subset so it can be treated as a (virtual) ImagePlus
		 */
		ImagePlus imp_sub = ImageJFunctions.wrapFloat( rai, "imageSubset" );
		imp_sub.setDisplayRange( displayMin, displayMax );
		imp_sub.show();

	
		/*
		 * This method exists for just visualization, but
		 * for interacting, the above 'wrapFloat' method
		 * will give you an ImagePlus that can be processed further if desired.
		 * 
		 * Note, however, that methods that change the values of the ImagePlus in 
		 * place may not work if the backend can't support it
		 * (e.g. fetching imagedata from a remote server) 
		 * 
		 * See also:
		 * https://github.com/imglib/imglib2-cache
		 */
		// ImageJFunctions.show( rai, "imageSubset" );
			
	}

}
