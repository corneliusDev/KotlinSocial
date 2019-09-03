package com.photoglyde.justincornelius.photoglyde.Utilities

import im.ene.toro.PlayerSelector
import im.ene.toro.ToroPlayer
import im.ene.toro.widget.Container
import java.util.*


    object PlayerSelectorOption : PlayerSelector {
        override fun reverse(): PlayerSelector {

            return reverse()
        }

        override fun select(
            container: Container,
            items: MutableList<ToroPlayer>
        ): List<ToroPlayer> {
            var count = items.size
            //    println("=====player count $count and ${items[0]}")
            var toSelect: List<ToroPlayer>
            if (count < 1) {
                println("==========player select1")
                toSelect = Collections.emptyList()
            } else {

                val firstOrder = items.get(0).playerOrder

                val span = 2
//                    count = Math.min(count, span / span)

                toSelect = ArrayList<ToroPlayer>()


               // println("==========player select2 ${staggeredLayoutManager.spanCount} and $count and $firstOrder")
                for (i in 0 until count) {
                    if (i < items.size) toSelect.add(items[i])
                }


            }

            return toSelect
        }
    }
