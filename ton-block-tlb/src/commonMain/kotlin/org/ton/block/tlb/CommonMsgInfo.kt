package org.ton.block.tlb

import org.ton.block.*
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun CommonMsgInfo.Companion.tlbCodec(): TlbCodec<CommonMsgInfo> = CommonMsgInfoTlbCombinator

private object CommonMsgInfoTlbCombinator : TlbCombinator<CommonMsgInfo>(
    IntMsgInfoTlbConstructor,
    ExtInMsgInfoTlbConstructor,
    ExtOutMsgInfoTlbConstructor
) {
    override fun getConstructor(value: CommonMsgInfo): TlbConstructor<out CommonMsgInfo> = when (value) {
        is CommonMsgInfo.IntMsgInfo -> IntMsgInfoTlbConstructor
        is CommonMsgInfo.ExtInMsgInfo -> ExtInMsgInfoTlbConstructor
        is CommonMsgInfo.ExtOutMsgInfo -> ExtOutMsgInfoTlbConstructor
    }

    object IntMsgInfoTlbConstructor : TlbConstructor<CommonMsgInfo.IntMsgInfo>(
        schema = "int_msg_info\$0 ihr_disabled:Bool bounce:Bool bounced:Bool " +
                "src:MsgAddressInt dest:MsgAddressInt " +
                "value:CurrencyCollection ihr_fee:Coins fwd_fee:Coins " +
                "created_lt:uint64 created_at:uint32 = CommonMsgInfo;"
    ) {
        private val msgAddressIntCodec = MsgAddressInt.tlbCodec()
        private val currencyCollectionCodec = CurrencyCollection.tlbCodec()
        private val coinsCodec = Coins.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder,
            value: CommonMsgInfo.IntMsgInfo,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeBit(value.ihrDisabled)
            storeBit(value.bounce)
            storeBit(value.bounced)
            storeTlb(value.src, msgAddressIntCodec)
            storeTlb(value.dest, msgAddressIntCodec)
            storeTlb(value.value, currencyCollectionCodec)
            storeTlb(value.ihrFee, coinsCodec)
            storeTlb(value.fwdFee, coinsCodec)
            storeUInt(value.createdLt, 64)
            storeUInt(value.createdAt, 32)
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): CommonMsgInfo.IntMsgInfo = cellSlice {
            val ihrDisabled = loadBit()
            val bounce = loadBit()
            val bounced = loadBit()
            val src = loadTlb(msgAddressIntCodec)
            val dest = loadTlb(msgAddressIntCodec)
            val value = loadTlb(currencyCollectionCodec)
            val ihrFee = loadTlb(coinsCodec)
            val fwdFee = loadTlb(coinsCodec)
            val createdLt = loadUInt(64).toLong()
            val createdAt = loadUInt(32).toInt()
            CommonMsgInfo.IntMsgInfo(
                ihrDisabled,
                bounce,
                bounced,
                src,
                dest,
                value,
                ihrFee,
                fwdFee,
                createdLt,
                createdAt
            )
        }
    }

    object ExtInMsgInfoTlbConstructor : TlbConstructor<CommonMsgInfo.ExtInMsgInfo>(
        schema = "ext_in_msg_info\$10 src:MsgAddressExt dest:MsgAddressInt import_fee:Coins = CommonMsgInfo;"
    ) {
        private val msgAddressExtCodec = MsgAddressExt.tlbCodec()
        private val msgAddressIntCodec = MsgAddressInt.tlbCodec()
        private val coinsCodec = Coins.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder,
            value: CommonMsgInfo.ExtInMsgInfo,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.src, msgAddressExtCodec)
            storeTlb(value.dest, msgAddressIntCodec)
            storeTlb(value.importFee, coinsCodec)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): CommonMsgInfo.ExtInMsgInfo = cellSlice {
            val src = loadTlb(msgAddressExtCodec)
            val dest = loadTlb(msgAddressIntCodec)
            val importFee = loadTlb(coinsCodec)
            CommonMsgInfo.ExtInMsgInfo(src, dest, importFee)
        }
    }

    object ExtOutMsgInfoTlbConstructor : TlbConstructor<CommonMsgInfo.ExtOutMsgInfo>(
        schema = "ext_out_msg_info\$11 src:MsgAddressInt dest:MsgAddressExt created_lt:uint64 created_at:uint32 = CommonMsgInfo;"
    ) {
        private val msgAddressIntCodec = MsgAddressInt.tlbCodec()
        private val msgAddressExtCodec = MsgAddressExt.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder,
            value: CommonMsgInfo.ExtOutMsgInfo,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.src, msgAddressIntCodec)
            storeTlb(value.dest, msgAddressExtCodec)
            storeUInt(value.createdLt, 64)
            storeUInt(value.createdAt, 32)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): CommonMsgInfo.ExtOutMsgInfo = cellSlice {
            val src = loadTlb(msgAddressIntCodec)
            val dest = loadTlb(msgAddressExtCodec)
            val createdLt = loadUInt(64).toLong()
            val createdAt = loadUInt(32).toInt()
            CommonMsgInfo.ExtOutMsgInfo(src, dest, createdLt, createdAt)
        }
    }
}