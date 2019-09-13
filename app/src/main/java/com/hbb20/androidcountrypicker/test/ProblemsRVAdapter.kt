package com.hbb20.androidcountrypicker.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.androidcountrypicker.R

class ProblemsRVAdapter(
    context: Context,
    private val rvItems: List<ProblemRVItem>
) :
        RecyclerView.Adapter<ProblemVH>() {
    private val fileNameViewType = 0
    private val categoryNameType = 1
    private val problemInfoType = 2
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProblemVH {
        val rowView = layoutInflater.inflate(
            when (viewType) {
                fileNameViewType -> R.layout.problem_filename_row
                categoryNameType -> R.layout.problem_category_row
                problemInfoType -> R.layout.problem_info_row
                else -> throw IllegalArgumentException("Unexpected viewType.")
            },
            parent,
            false
        )

        return when (viewType) {
            fileNameViewType -> ProblemFileVH(rowView)
            categoryNameType -> ProblemCategoryVH(rowView)
            problemInfoType -> ProblemInfoVH(rowView)
            else -> throw IllegalArgumentException("Unexpected viewType.")
        }
    }

    override fun getItemCount(): Int {
        return rvItems.size
    }

    override fun onBindViewHolder(
        holder: ProblemVH,
        position: Int
    ) {
        when (holder) {
            is ProblemFileVH -> holder.bindItem(rvItems[position] as ProblemFileRVItem)
            is ProblemCategoryVH -> holder.bindItem(rvItems[position] as ProblemCategoryRVItem)
            is ProblemInfoVH -> holder.bindItem(rvItems[position] as ProblemInfoRVItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (rvItems[position]) {
            is ProblemFileRVItem -> fileNameViewType
            is ProblemCategoryRVItem -> categoryNameType
            is ProblemInfoRVItem -> problemInfoType
        }
    }

}

sealed class ProblemRVItem
class ProblemFileRVItem(val fileName: String) : ProblemRVItem()
class ProblemCategoryRVItem(val categoryName: String) : ProblemRVItem()
class ProblemInfoRVItem(val problem: Problem) : ProblemRVItem()

sealed class ProblemVH(itemView: View) : RecyclerView.ViewHolder(itemView)
class ProblemFileVH(itemView: View) : ProblemVH(itemView) {
    val tvFileName = itemView.findViewById<TextView>(R.id.tvFileName)
    fun bindItem(problemFileRVItem: ProblemFileRVItem) {
        tvFileName.text = problemFileRVItem.fileName
    }
}

class ProblemCategoryVH(itemView: View) : ProblemVH(itemView) {
    private val tvCategoryName = itemView.findViewById<TextView>(R.id.tvCategory)
    fun bindItem(problemCategoryRVItem: ProblemCategoryRVItem) {
        tvCategoryName.text = problemCategoryRVItem.categoryName
    }
}

class ProblemInfoVH(itemView: View) : ProblemVH(itemView) {
    private val context = itemView.context
    private val tvProblem = itemView.findViewById<TextView>(R.id.tvProblem)
    private val imgProblemLevel = itemView.findViewById<ImageView>(R.id.imgProblemLevel)
    private val errorColor = ContextCompat.getColor(context, R.color.error_color)
    private val warningColor = ContextCompat.getColor(context, R.color.warning_color)

    fun bindItem(problemInfoRVItem: ProblemInfoRVItem) {
        when (problemInfoRVItem.problem.category) {
            ProblemCategory.UNVERIFIED_ENTRIES -> {
                tvProblem.setTextColor(warningColor)
                imgProblemLevel.setImageResource(R.drawable.ic_warning)
                imgProblemLevel.setColorFilter(warningColor)
            }
            else -> {
                tvProblem.setTextColor(errorColor)
                imgProblemLevel.setImageResource(R.drawable.ic_error_outline)
                imgProblemLevel.setColorFilter(errorColor)
            }
        }
        tvProblem.text = problemInfoRVItem.problem.solution
    }
}

