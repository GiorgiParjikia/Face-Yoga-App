package ru.netology.faceyoga.data.seed

object SeedPlansEasy {

    private fun ex(title: String, reps: Int = 10) =
        DayExerciseSeed.Reps(title = title, reps = reps)

    private fun timer(title: String, seconds: Int) =
        DayExerciseSeed.Timer(title = title, seconds = seconds)

    private fun relax(seconds: Int = 60) =
        DayExerciseSeed.Timer(title = "Relax", seconds = seconds)

    // ex("Jawline Lift", reps = 5)
    //timer("Jawline Lift", seconds = 120),
    // или timer("Smiling Resistance", 90)

    fun build(): Map<Int, List<DayExerciseSeed>> =
        mapOf(
            1 to listOf(
                ex("Chin and Jawline Lift", reps = 5),
                ex("Jawline & Face Lift", reps = 2),
                ex("Nasolabial Fold Smoothing", reps = 10),
                relax(60),
                ex("Cheek Lift", reps = 10),
                ex("Lymphatic Drainage Massage", reps = 10),
            ),
            2 to listOf(
                ex("Forehead Lifting Massage", reps = 10),
                ex("Revitalizing Face Massage", reps = 10),
                ex("Chin and Jawline Lift", reps = 10),
                relax(60),
                ex("Face & Neck Lift", reps = 10),
                ex("Jawline & Face Lift", reps = 10),
            ),
            3 to listOf(
                ex("Eye Muscle Strengthening", reps = 10),
                ex("Eye Contour Strengthening", reps = 10),
                ex("Horizontal Lip Stretch", reps = 10),
                relax(60),
                ex("Finger Piano Massage from Cheeks to Temples", reps = 10),
                ex("Vertical Wave Forehead Massage", reps = 10),
                ex("Lip-Tucked Smile Exercise", reps = 10),
            ),
            4 to listOf(
                ex("Fists from Chin to Ears", reps = 10),
                ex("Back-of-hand glide from chin to ears", reps = 10),
                ex("Blinking with Eye Corner Stretch", reps = 10),
                relax(60),
                ex("Jawline Lift", reps = 10),
                ex("Mid-Face Acupressure", reps = 10),
                ex("Jawline & Face Lift", reps = 10),
            ),
            5 to listOf(
                ex("Smoothing Nasolabial Folds", reps = 10),
                ex("Gentle Facial Massage with Tapping and Smoothing", reps = 10),
                ex("Horizontal Lip Stretch", reps = 10),
                ex("Cheek Lift & Glow", reps = 10),
                relax(60),
                ex("Fists from Chin to Ears", reps = 10),
                ex("Temple Area Massage", reps = 10),
                ex("Intense Blinking with Deep Eye Corner Stretch", reps = 10),
            ),
            6 to listOf(
                ex("Pressure Point Eye Relaxation", reps = 10),
                ex("Nasolabial to Temples Smoothing", reps = 10),
                relax(60),
                ex("Chin Smoothing", reps = 10),
                ex("Ear Pull Jawline Lift", reps = 10),
                ex("Jawline Sculpting", reps = 10),
                relax(60),
                ex("Cheek Elevation Massage", reps = 10),
                ex("Cheek Lift & Glow", reps = 10),
            ),
            7 to listOf(
                ex("Mouth Corner Lift", reps = 10),
                ex("Chin Smoothing", reps = 10),
                ex("Horse Lips Exercise", reps = 10),
                relax(60),
                ex("Pencil Lip Workout", reps = 10),
                ex("Lip Corner Lift", reps = 10),
                relax(60),
                ex("Eyebrow Pinching Massage", reps = 10),
                ex("Forehead and Temple Relaxation Massage", reps = 10),
                ex("Air Kiss Stretch", reps = 10),
            ),
            8 to listOf(
                ex("Fixed Air Kiss", reps = 10),
                ex("Comprehensive Exercise for Facial Smoothness and Tone", reps = 10),
                ex("Toning Massage for Forehead", reps = 10),
                relax(60),
                ex("Cheek Elevation Massage", reps = 10),
                ex("Pencil Lip Workout", reps = 10),
                relax(60),
                ex("Forehead and Temple Relaxation Routine", reps = 10),
                ex("Air Rolling Exercise", reps = 10),
                ex("Cheek Massage with Fist Exercise", reps = 10),
            ),
            9 to listOf(
                ex("Lymphatic Drainage Face & Neck Massage Exercise", reps = 10),
                ex("Chin and Neck Lift", reps = 10),
                ex("Air Rolling with Hand Pressure", reps = 10),
                relax(60),
                ex("Chin Support", reps = 10),
                ex("Cheek Tongue Workout", reps = 10),
                ex("Cheek Massage with Fist Exercise", reps = 10),
                relax(60),
                ex("Fixed Air Kiss", reps = 10),
                ex("Cheek Smoothing", reps = 10),
                ex("Eyebrow Pressure", reps = 10),
            ),
            10 to listOf(
                ex("Forehead Pressure Taps", reps = 10),
                ex("Index Fingers to Temples", reps = 10),
                ex("Eyebrow Pinching", reps = 10),
                relax(60),
                ex("Pressure Points Under Eyes", reps = 10),
                ex("Chin and Neck Lift", reps = 10),
                ex("Pressure Between Brows", reps = 10),
                relax(60),
                ex("Cheek Acupressure Massage", reps = 10),
                ex("Chin Support", reps = 10),
                ex("Cheekbone Lift", reps = 10),
            )
        )
}
