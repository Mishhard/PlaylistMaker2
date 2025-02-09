import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Track

class TracksAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()
    private var onItemClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun setOnItemClickListener(listener: (Track) -> Unit) {
        onItemClickListener = listener
    }
}