package ru.netology.faceyoga.data.seed

import ru.netology.faceyoga.data.db.ExerciseEntity
import ru.netology.faceyoga.data.model.ExerciseType
import ru.netology.faceyoga.data.model.Zone

object SeedExercises {
    val exercises: List<ExerciseEntity> = listOf(
        ExerciseEntity(
            title = "Relax",
            zone = Zone.FULL_FACE,
            description = "Rest and relax",
            type = ExerciseType.TIMER,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/Relax.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/break.jpg"
        ),
        ExerciseEntity(
            title = "Chin and Jawline Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/1.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/1.jpg"

        ),
        ExerciseEntity(
            title = "Jawline & Face Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/2.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/2.jpg"
        ),
        ExerciseEntity(
            title = "Nasolabial Fold Smoothing",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/3.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/3.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Lift",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/4.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/4.jpg"
        ),
        ExerciseEntity(
            title = "Lymphatic Drainage Massage",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/5.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/5.jpg"
        ),
        ExerciseEntity(
            title = "Forehead Lifting Massage",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/6.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/6.jpg"
        ),
        ExerciseEntity(
            title = "Revitalizing Face Massage",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/7.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/7.jpg"
        ),
        ExerciseEntity(
            title = "Face & Neck Lift",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/8.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/8.jpg"
        ),
        ExerciseEntity(
            title = "Eye Muscle Strengthening",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/9.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/9.jpg"
        ),
        ExerciseEntity(
            title = "Eye Contour Strengthening",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/10.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/10.jpg"
        ),
        ExerciseEntity(
            title = "Blinking with Eye Corner Stretch",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/11.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/11.jpg"
        ),
        ExerciseEntity(
            title = "Intense Blinking with Deep Eye Corner Stretch",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/12.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/12.jpg"
        ),
        ExerciseEntity(
            title = "Horizontal Lip Stretch",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/13.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/13.jpg"
        ),
        ExerciseEntity(
            title = "Finger Piano Massage from Cheeks to Temples",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/14.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/14.jpg"
        ),
        ExerciseEntity(
            title = "Vertical Wave Forehead Massage",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/15.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/15.jpg"
        ),
        ExerciseEntity(
            title = "Lip-Tucked Smile Exercise",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/16.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/16.jpg"
        ),
        ExerciseEntity(
            title = "Pencil Lip Workout",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            requiresItem = true,
            requiredItemKey = "pencil",
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/17.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/17.jpg"
    ),
        ExerciseEntity(
            title = "Fists from Chin to Ears",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/18.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/18.jpg"
        ),
        ExerciseEntity(
            title = "Back-of-hand glide from chin to ears",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/19.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/19.jpg"
        ),
        ExerciseEntity(
            title = "Jawline Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/20.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/20.jpg"
        ),
        ExerciseEntity(
            title = "Mid-Face Acupressure",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/21.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/21.jpg"
        ),
        ExerciseEntity(
            title = "Smoothing Nasolabial Folds",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/22.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/22.jpg"
        ),
        ExerciseEntity(
            title = "Nasolabial to Temples Smoothing",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/23.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/23.jpg"
        ),
        ExerciseEntity(
            title = "Gentle Facial Massage with Tapping and Smoothing",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/24.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/24.jpg"
        ),
        ExerciseEntity(
            title = "Forehead and Temple Relaxation Massage",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/25.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/25.jpg"
        ),
        ExerciseEntity(
            title = "Forehead and Temple Relaxation Routine",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/26.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/26.jpg"
        ),
        ExerciseEntity(
            title = "Comprehensive Exercise for Facial Smoothness and Tone",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/27.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/27.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Lift & Glow",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/28.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/28.jpg"
        ),
        ExerciseEntity(
            title = "Temple Area Massage",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/29.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/29.jpg"
        ),
        ExerciseEntity(
            title = "Toning Massage for Forehead",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/30.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/30.jpg"
        ),
        ExerciseEntity(
            title = "Eyebrow Pinching Massage",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/31.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/31.jpg"
        ),
        ExerciseEntity(
            title = "Pressure Point Eye Relaxation",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/32.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/32.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Elevation Massage",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/33.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/33.jpg"
        ),
        ExerciseEntity(
            title = "Ear Pull Jawline Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/34.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/34.jpg"
        ),
        ExerciseEntity(
            title = "Chin Smoothing",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/35.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/35.jpg"
        ),
        ExerciseEntity(
            title = "Jawline Sculpting",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/36.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/36.jpg"
        ),
        ExerciseEntity(
            title = "Mouth Corner Lift",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/37.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/37.jpg"
        ),
        ExerciseEntity(
            title = "Air Kiss Stretch",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/38.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/38.jpg"
        ),
        ExerciseEntity(
            title = "Fixed Air Kiss",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/39.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/39.jpg"
        ),
        ExerciseEntity(
            title = "Lip Corner Lift",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/40.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/40.jpg"
        ),
        ExerciseEntity(
            title = "Horse Lips Exercise",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/41.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/41.jpg"
        ),
        ExerciseEntity(
            title = "Air Rolling Exercise",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/42.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/42.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Massage with Fist Exercise",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/43.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/43.jpg"
        ),
        ExerciseEntity(
            title = "Lymphatic Drainage Face & Neck Massage Exercise",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/44.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/44.jpg"
        ),
        ExerciseEntity(
            title = "Chin and Neck Lift",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/45.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/45.jpg"
        ),
        ExerciseEntity(
            title = "Air Rolling with Hand Pressure",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/46.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/46.jpg"
        ),
        ExerciseEntity(
            title = "Chin Support",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/47.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/47.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Tongue Workout",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/48.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/48.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Smoothing",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/49.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/49.jpg"
        ),
        ExerciseEntity(
            title = "Eyebrow Pressure",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/50.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/50.jpg"
        ),
        ExerciseEntity(
            title = "Forehead Pressure Taps",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/51.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/51.jpg"
        ),
        ExerciseEntity(
            title = "Temple Pressure",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/52.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/52.jpg"
        ),
        ExerciseEntity(
            title = "Eyebrow Pinching",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/53.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/53.jpg"
        ),
        ExerciseEntity(
            title = "Pressure Points Under Eyes",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/54.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/54.jpg"
        ),
        ExerciseEntity(
            title = "Index Fingers to Temples",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/55.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/55.jpg"
        ),
        ExerciseEntity(
            title = "Pressure Between Brows",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/56.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/56.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Acupressure Massage",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/57.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/57.jpg"
        ),
        ExerciseEntity(
            title = "Cheekbone Lift",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/58.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/58.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Pinches",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/59.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/59.jpg"
        ),
        ExerciseEntity(
            title = "Cheekbone Pressure Points",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/60.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/60.jpg"
        ),
        ExerciseEntity(
            title = "Corner Lift",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/61.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/61.jpg"
        ),
        ExerciseEntity(
            title = "Smiling Resistance",
            zone = Zone.LIPS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/62.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/62.jpg"
        ),
        ExerciseEntity(
            title = "Nasolabial Drainage",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/63.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/63.jpg"
        ),
        ExerciseEntity(
            title = "Chin Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/64.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/64.jpg"
        ),
        ExerciseEntity(
            title = "Masseter Release",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/65.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/65.jpg"
        ),
        ExerciseEntity(
            title = "Stroking from Chin to Cheek",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/66.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/66.jpg"
        ),
        ExerciseEntity(
            title = "Cheek Massage with Open Mouth",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/67.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/67.jpg"
        ),
        ExerciseEntity(
            title = "Cheek and Jawline Smoothing Massage",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/68.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/68.jpg"
        ),
        ExerciseEntity(
            title = "Lip Ring",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/69.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/69.jpg"
        ),
        ExerciseEntity(
            title = "Forehead-Supported Fish Face",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/70.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/70.jpg"
        ),
        ExerciseEntity(
            title = "Chin-to-Cheek Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/71.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/71.jpg"
        ),
        ExerciseEntity(
            title = "Corner Lift Exercise",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/72.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/72.jpg"
        ),
        ExerciseEntity(
            title = "Facial Lifting Massage",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/73.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/73.jpg"
        ),
        ExerciseEntity(
            title = "Scalp Stimulation",
            zone = Zone.FULL_FACE, //TODO Не совсем так (оно направленно на кожу головы, надо прописать в дискрипшне
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/74.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/74.jpg"
        ),
        ExerciseEntity(
            title = "Forehead Pinches",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/75.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/75.jpg"
        ),
        ExerciseEntity(
            title = "Smoothing and Pressing Forehead Motions",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/76.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/76.jpg"
        ),
        ExerciseEntity(
            title = "Forehead Smoothing",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/77.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/77.jpg"
        ),
        ExerciseEntity(
            title = "Glabella Smoothing",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/78.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/78.jpg"
        ),
        ExerciseEntity(
            title = "Eye Corner Lifting",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/79.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/79.jpg"
        ),
        ExerciseEntity(
            title = "Nasolabial Muscle Lift",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/80.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/80.jpg"
        ),
        ExerciseEntity(
            title = "Lymphatic Drainage Massage for Face and Neck",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/81.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/81.jpg"
        ),
        ExerciseEntity(
            title = "Palm Eye Relaxation",
            zone = Zone.EYES,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/82.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/82.jpg"
        ),
        ExerciseEntity(
            title = "Lower Jaw Contour Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/83.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/83.jpg"
        ),
        ExerciseEntity(
            title = "Tapping from Upper Chest to Chin",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/84.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/84.jpg"
        ),
        ExerciseEntity(
            title = "Jawline Skin Twisting with Knuckles",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/85.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/85.jpg"
        ),
        ExerciseEntity(
            title = "Air Rolling in Cheeks",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/86.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/86.jpg"
        ),
        ExerciseEntity(
            title = "Closed-Mouth Smile",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/87.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/87.jpg"
        ),
        ExerciseEntity(
            title = "Kiss the Ceiling",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/88.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/88.jpg"
        ),
        ExerciseEntity(
            title = "Forehead Lift with Blinking",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/89.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/89.jpg"
        ),
        ExerciseEntity(
            title = "Head Turns",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/90.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/90.jpg"
        ),
        ExerciseEntity(
            title = "Gentle Tapping on the Face",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/91.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/91.jpg"
        ),
        ExerciseEntity(
            title = "Head Lifts and Drops",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/92.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/92.jpg"
        ),
        ExerciseEntity(
            title = "Neck and Jawline Lift with Ear Pull",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/93.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/93.jpg"
        ),
        ExerciseEntity(
            title = "Lions Gaze",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/94.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/94.jpg"
        ),
        ExerciseEntity(
            title = "Side Neck Stretch (Left)",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/95.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/95.jpg"
        ),
        ExerciseEntity(
            title = "Side Neck Stretch (Right)",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/95_2.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/95_2.jpg"
        ),
        ExerciseEntity(
            title = "Smiling with Resistance",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/96.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/96.jpg"
        ),
        ExerciseEntity(
            title = "Kisses to the Ceiling",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/97.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/97.jpg"
        ),
        ExerciseEntity(
            title = "Graceful Smile",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/98.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/98.jpg"
        ),
        ExerciseEntity(
            title = "Defined Jawline Exercise",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/99.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/99.jpg"
        ),
        ExerciseEntity(
            title = "Chin Line Finger Lift",
            zone = Zone.NECK,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/100.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/100.jpg"
        ),
        ExerciseEntity(
            title = "Neck Awakening",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/101.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/101.jpg"
        ),
        ExerciseEntity(
            title = "Fist Lift",
            zone = Zone.JAWLINE_CHIN,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/102.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/102.jpg"
        ),
        ExerciseEntity(
            title = "Air Balloon",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/103.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/103.jpg"
        ),
        ExerciseEntity(
            title = "Air Wave",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/104.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/104.jpg"
        ),
        ExerciseEntity(
            title = "Smooth Smile Lines",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/105.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/105.jpg"
        ),
        ExerciseEntity(
            title = "Nasolabial Zone Massage",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/106.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/106.jpg"
        ),
        ExerciseEntity(
            title = "Tear Trough & Cheek Line Smoothing (Left)",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/107.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/107.jpg"
        ),
        ExerciseEntity(
            title = "Tear Trough & Cheek Line Smoothing (Right)",
            zone = Zone.CHEEKS,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/107_2.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/107_2.jpg"
        ),
        ExerciseEntity(
            title = "Blinking with Forehead Hold",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/108.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/108.jpg"
        ),
        ExerciseEntity(
            title = "Forehead Wrinkle Smoothing (Right)",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/109.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/109.jpg"
        ),
        ExerciseEntity(
            title = "Forehead Wrinkle Smoothing (Left)",
            zone = Zone.FOREHEAD,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/109_2.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/109_2.jpg"
        ),
        ExerciseEntity(
            title = "Lymphatic Drainage Self-Massage Routine",
            zone = Zone.FULL_FACE,
            description = "",
            type = ExerciseType.REPS,
            videoUri = "gs://face-yoga-mvp.firebasestorage.app/Videos (Mobile)/110.mp4",
            previewImageUri = "gs://face-yoga-mvp.firebasestorage.app/Preview_Image/110.jpg"
        ),
    )
}