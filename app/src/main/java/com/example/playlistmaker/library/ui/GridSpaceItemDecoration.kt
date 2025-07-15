import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DifferentVerticalHorizontalDecoration(
    private val verticalSpace: Int,
    private val horizontalSpace: Int,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        val layoutManager = parent.layoutManager

        if (layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val column = position % spanCount

            if (includeEdge) {
                outRect.left = horizontalSpace - column * horizontalSpace / spanCount
                outRect.right = (column + 1) * horizontalSpace / spanCount

                if (position < spanCount) {
                    outRect.top = verticalSpace
                }
                outRect.bottom = verticalSpace
            } else {
                outRect.left = column * horizontalSpace / spanCount
                outRect.right = horizontalSpace - (column + 1) * horizontalSpace / spanCount
                if (position >= spanCount) {
                    outRect.top = verticalSpace
                }
            }
        } else {
            outRect.left = horizontalSpace
            outRect.right = horizontalSpace

            if (position < itemCount - 1) {
                outRect.bottom = verticalSpace
            }

            if (includeEdge && position == 0) {
                outRect.top = verticalSpace
            }
        }
    }
}
