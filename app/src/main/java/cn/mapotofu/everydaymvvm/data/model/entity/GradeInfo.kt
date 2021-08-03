package cn.mapotofu.everydaymvvm.data.model.entity

data class GradeInfo(
    val date: String,
    val scoreAvg: Float,
    val avgScoreWeighted: Float,
    val creditSum: Float,
    val GPAAvg: Float,
    val avgGPAWeighted: Float
)