// MainActivityFragment.java
// MainActivityFragment creates and manages a CannonView
package pl.pjatk.pamo.kotlincalculatorbmi.game


import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.pjatk.pamo.calculatorbmi.R


class MainActivityFragment : Fragment() {
    private var cannonView // custom view to display the game
            : CannonView? = null

    // called when Fragment's view needs to be created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // inflate the fragment_main.xml layout
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        // get a reference to the CannonView
        cannonView = view.findViewById<View>(R.id.cannonView) as CannonView
        return view
    }

    // set up volume control once Activity is created
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // allow volume buttons to set game volume
        activity!!.volumeControlStream = AudioManager.STREAM_MUSIC
    }

    // when MainActivity is paused, terminate the game
    override fun onPause() {
        super.onPause()
        cannonView!!.stopGame() // terminates the game
    }

    // when MainActivity is paused, MainActivityFragment releases resources
    override fun onDestroy() {
        super.onDestroy()
        cannonView!!.releaseResources()
    }
}

/*********************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and * Pearson Education, *
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
 * book have used their * best efforts in preparing the book. These efforts      *
 * include the * development, research, and testing of the theories and programs *
 * * to determine their effectiveness. The authors and publisher make * no       *
 * warranty of any kind, expressed or implied, with regard to these * programs   *
 * or to the documentation contained in these books. The authors * and publisher *
 * shall not be liable in any event for incidental or * consequential damages in *
 * connection with, or arising out of, the * furnishing, performance, or use of  *
 * these programs.                                                               *
 *********************************************************************************/


//
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//
//import pl.pjatk.pamo.calculatorbmi.R;
//
//public class MainActivityFragment extends Fragment {
//   private pl.pjatk.pamo.calculatorbmi.game.CannonView cannonView; // custom view to display the game
//
//   // called when Fragment's view needs to be created
//   @Override
//   public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                            Bundle savedInstanceState) {
//      super.onCreateView(inflater, container, savedInstanceState);
//
//      // inflate the fragment_main.xml layout
//      View view =
//         inflater.inflate(R.layout.fragment_main, container, false);
//
//      // get a reference to the CannonView
//      cannonView = (pl.pjatk.pamo.calculatorbmi.game.CannonView) view.findViewById(R.id.cannonView);
//      return view;
//   }
//
//   // set up volume control once Activity is created
//   @Override
//   public void onActivityCreated(Bundle savedInstanceState) {
//      super.onActivityCreated(savedInstanceState);
//
//      // allow volume buttons to set game volume
//      getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
//   }
//
//   // when MainActivity is paused, terminate the game
//   @Override
//   public void onPause() {
//      super.onPause();
//      cannonView.stopGame(); // terminates the game
//   }
//
//   // when MainActivity is paused, MainActivityFragment releases resources
//   @Override
//   public void onDestroy() {
//      super.onDestroy();
//      cannonView.releaseResources();
//   }
//}
//
///*********************************************************************************
// * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and * Pearson Education, *
// * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
// * book have used their * best efforts in preparing the book. These efforts      *
// * include the * development, research, and testing of the theories and programs *
// * * to determine their effectiveness. The authors and publisher make * no       *
// * warranty of any kind, expressed or implied, with regard to these * programs   *
// * or to the documentation contained in these books. The authors * and publisher *
// * shall not be liable in any event for incidental or * consequential damages in *
// * connection with, or arising out of, the * furnishing, performance, or use of  *
// * these programs.                                                               *
// *********************************************************************************/
