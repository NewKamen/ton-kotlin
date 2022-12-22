package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

sealed interface TextChunkRef {
    companion object {
        fun tlbCombinator(n: Int): TlbCombinator<TextChunkRef> = if (n == 0) {
            ChunkRefEmpty.tlbConstructor()
        } else {
            ChunkRef.tlbConstructor(n)
        } as TlbCombinator<TextChunkRef>
    }
}