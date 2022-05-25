package org.ton.block.tlb

import org.ton.block.VmStackValue
import org.ton.block.VmTuple
import org.ton.block.VmTupleRef
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmTupleRef.Companion.tlbCodec(): TlbCodec<VmTupleRef> = VmTupleRefTlbCombinator()

private class VmTupleRefTlbCombinator : TlbCombinator<VmTupleRef>() {
    private val nilConstructor by lazy { VmTupleRefNilTlbConstructor() }
    private val singleConstructor by lazy { VmTupleRefSingleTlbConstructor() }
    private val anyConstructor by lazy { VmTupleRefAnyTlbConstructor() }

    override val constructors: List<TlbConstructor<out VmTupleRef>> by lazy {
        listOf(nilConstructor, singleConstructor, anyConstructor)
    }

    override fun getConstructor(value: VmTupleRef): TlbConstructor<out VmTupleRef> = when (value) {
        is VmTupleRef.Nil -> nilConstructor
        is VmTupleRef.Single -> singleConstructor
        is VmTupleRef.Any -> anyConstructor
    }

    override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): VmTupleRef {
        return when (param) {
            0 -> nilConstructor.decode(cellSlice, param, negativeParam)
            1 -> singleConstructor.decode(cellSlice, param, negativeParam)
            else -> anyConstructor.decode(cellSlice, param, negativeParam)
        }
    }

    private class VmTupleRefNilTlbConstructor : TlbConstructor<VmTupleRef.Nil>(
        schema = "vm_tupref_nil\$_ = VmTupleRef 0;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder, value: VmTupleRef.Nil, param: Int, negativeParam: (Int) -> Unit
        ) {
        }

        override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): VmTupleRef.Nil {
            return VmTupleRef.Nil
        }
    }

    private class VmTupleRefSingleTlbConstructor : TlbConstructor<VmTupleRef.Single>(
        schema = "vm_tupref_single\$_ entry:^VmStackValue = VmTupleRef 1;"
    ) {
        private val vmStackValueCodec by lazy { VmStackValue.tlbCodec() }

        override fun encode(
            cellBuilder: CellBuilder, value: VmTupleRef.Single, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef {
                storeTlb(value.entry, vmStackValueCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmTupleRef.Single = cellSlice {
            val entry = loadRef {
                loadTlb(vmStackValueCodec)
            }
            VmTupleRef.Single(entry)
        }
    }

    private class VmTupleRefAnyTlbConstructor : TlbConstructor<VmTupleRef.Any>(
        schema = "vm_tupref_any\$_ {n:#} ref:^(VmTuple (n + 2)) = VmTupleRef (n + 2);"
    ) {
        private val vmTupleCodec by lazy { VmTuple.tlbCodec() }

        override fun encode(
            cellBuilder: CellBuilder, value: VmTupleRef.Any, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            val n = param - 2
            storeRef {
                storeTlb(value.ref, vmTupleCodec, n)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmTupleRef.Any = cellSlice {
            val n = param - 2
            val ref = loadRef {
                loadTlb(vmTupleCodec, n)
            }
            VmTupleRef.Any(ref)
        }
    }
}
