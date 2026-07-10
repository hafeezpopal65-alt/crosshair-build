package com.shivam.crosshairstudio

data class CrosshairPreset(
    val id: String,
    val a1: Int, val a2: Int, val a3: Int, val a4: Int,
    val radius: Int, val length: Int, val thickness: Int,
    val name: String
)

object CrosshairDatabase {
    val presets: List<CrosshairPreset> = buildList {
        val fixed = listOf(
            listOf(0,90,180,270,5,20,3,"CLASSIC PLUS"),
            listOf(0,0,0,0,0,1,10,"VALO DOT"),
            listOf(45,135,225,315,12,25,4,"X-REAPER"),
            listOf(135,135,225,360,60,20,3,"Y-DASNY"),
            listOf(150,210,150,210,8,45,3,"V-WINGS"),
            listOf(0,90,0,90,0,40,6,"L-SNIPER"),
            listOf(90,180,270,270,0,40,5,"T-OFFICER"),
            listOf(45,225,90,270,15,25,8,"Z-BOLT"),
            listOf(45,135,225,315,30,1,15,"SQUARE"),
            listOf(0,45,180,225,25,2,12,"CIRCLE"),
            listOf(180,270,90,180,20,30,4,"U-BRACKET"),
            listOf(90,270,90,270,10,90,2,"HORIZON"),
            listOf(0,180,0,180,10,80,2,"VERTICAL"),
            listOf(0,90,180,270,5,60,12,"SHURIKEN"),
            listOf(0,120,240,0,20,30,6,"TRI-POWER"),
            listOf(30,90,150,210,40,10,14,"HEX-GATE"),
            listOf(90,270,180,180,20,70,10,"TANK-S"),
            listOf(0,180,0,180,60,120,2,"GAP-X"),
            listOf(0,30,330,0,15,45,5,"ARROW-U"),
            listOf(150,210,30,330,12,40,3,"M-WINGS"),
            listOf(45,135,200,340,18,28,5,"K-POINT"),
            listOf(0,90,180,270,35,35,2,"DIAMOND"),
            listOf(0,90,180,270,2,10,4,"MICRO"),
            listOf(22,67,112,157,45,5,12,"OCTA-G"),
            listOf(135,225,150,210,8,60,2,"DOUBLE-V"),
            listOf(90,180,270,0,0,35,5,"TOP-T"),
            listOf(135,225,135,225,25,45,6,"BRACKET-L"),
            listOf(45,315,45,315,25,45,6,"BRACKET-R"),
            listOf(30,150,210,330,12,55,3,"SPIDER"),
            listOf(0,180,0,180,15,55,12,"PAR-V"),
            listOf(90,270,90,270,15,55,12,"FLAT-B"),
            listOf(0,90,180,270,45,12,22,"VOID-C"),
            listOf(0,90,180,270,0,180,1,"NEEDLE"),
            listOf(150,210,150,210,2,35,12,"CHEV-U"),
            listOf(45,135,45,135,35,65,3,"GATE-K"),
            listOf(90,270,90,270,8,110,18,"RAIL-G"),
            listOf(0,120,240,360,15,25,14,"HAZARD"),
            listOf(45,135,225,315,60,8,18,"LOCK-C"),
            listOf(0,90,180,270,0,22,22,"FAT-P"),
            listOf(160,200,20,340,12,42,4,"BI-ARROW"),
            listOf(45,135,225,315,4,10,3,"MINI-X"),
            listOf(0,90,180,270,25,15,4,"OW-SYMM"),
            listOf(0,180,90,270,10,30,2,"APEX-T"),
            listOf(45,135,225,315,10,75,7,"TITAN-H"),
            listOf(45,135,225,315,90,20,1,"GHOST-W"),
            listOf(0,45,90,135,8,8,14,"SCAT-L"),
            listOf(0,180,0,180,0,140,5,"BEAM-V"),
            listOf(90,270,90,270,0,140,5,"BEAM-H"),
            listOf(0,90,180,270,15,5,12,"HYBRID-D"),
            listOf(45,135,225,315,25,45,2,"CYBER-2K"),
        )
        for ((i, v) in fixed.withIndex()) {
            val id = (i + 1).toString().padStart(3, '0')
            @Suppress("UNCHECKED_CAST")
            val vi = v as List<Any>
            add(CrosshairPreset(id, vi[0] as Int, vi[1] as Int, vi[2] as Int, vi[3] as Int,
                vi[4] as Int, vi[5] as Int, vi[6] as Int, vi[7] as String))
        }
        for (i in 51..100) {
            val id = i.toString().padStart(3, '0')
            add(CrosshairPreset(id, 0, 90, 180, 270, 15, 15, 5, "PRO-$i"))
        }
    }
}
