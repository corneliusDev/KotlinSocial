package com.photoglyde.justincornelius.photoglyde.Utilities

import im.ene.toro.PlayerSelector
import im.ene.toro.ToroPlayer
import im.ene.toro.widget.Container
import java.util.*


    object PlayerSelectorOption : PlayerSelector {
        override fun reverse(): PlayerSelector {

            return reverse()
        }

        override fun select(container: Container, items: MutableList<ToroPlayer>): List<ToroPlayer> {

            var count = items.size

            var toSelect: List<ToroPlayer>

            if (count < 1) {

                toSelect = Collections.emptyList()

            } else {

                toSelect = ArrayList<ToroPlayer>()

                for (i in 0 until count) {
                    if (i < items.size) toSelect.add(items[i])
                }

            }

            return toSelect
        }
    }
