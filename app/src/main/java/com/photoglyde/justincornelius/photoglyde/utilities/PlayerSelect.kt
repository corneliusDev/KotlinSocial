package com.photoglyde.justincornelius.photoglyde.utilities

import im.ene.toro.PlayerSelector
import im.ene.toro.ToroPlayer
import im.ene.toro.widget.Container
import java.util.*


object PlayerSelectorOption : PlayerSelector {

    override fun reverse(): PlayerSelector {

        return reverse()

    }

    override fun select(container: Container, items: MutableList<ToroPlayer>): List<ToroPlayer> {

        val size = items.size

        val toSelect: List<ToroPlayer>

        if (size < 1) toSelect = Collections.emptyList() else {

            toSelect = ArrayList()

            for (i in 0 until size) {

                if (i < items.size) toSelect.add(items[i])

            }

        }

        return toSelect
    }
}
