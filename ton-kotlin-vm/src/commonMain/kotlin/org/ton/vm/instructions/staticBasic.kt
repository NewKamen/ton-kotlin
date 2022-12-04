package org.ton.vm.instructions

import org.ton.asm.stackbasic.*
import org.ton.block.Maybe
import org.ton.vm.VmState
import org.ton.vm.VmStateException
import org.ton.vm.VmStateRunning

internal fun executeNop(state: VmStateRunning, instruction: NOP): VmState = state

internal fun executeSwap(state: VmStateRunning, instruction: SWAP): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val s0 = stack.pop()
        val s1 = stack.pop()

        stack.push(s0)
        stack.push(s1)

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeXCHG_OI(state: VmStateRunning, instruction: XCHG_0I): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < instruction.i.toInt()) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.interchange(instruction.i)

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeXCHG_IJ(state: VmStateRunning, instruction: XCHG_IJ): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < instruction.i.toInt() || stack.depth < instruction.j.toInt()) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.interchange(instruction.i, instruction.j)

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeXCHG_OI_LONG(state: VmStateRunning, instruction: XCHG_0I_LONG): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < instruction.ii.toInt()) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.interchange(instruction.ii)

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeXCHG_1I(state: VmStateRunning, instruction: XCHG_1I): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth > 1 || stack.depth < instruction.i.toInt()) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.interchange(1u, instruction.i)

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executePUSH(state: VmStateRunning, instruction: PUSH): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null ||   stack.depth < instruction.i.toInt()) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val value = stack[instruction.i.toInt()]
        stack.push(value)
        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeDUP(state: VmStateRunning, instruction: DUP): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null ||   stack.depth < 1) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.push(stack[0])
        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeOVER(state: VmStateRunning, instruction: OVER): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null ||   stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val value = stack[1]
        stack.push(value)
        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executePOP(state: VmStateRunning, instruction: POP): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null ||   stack.depth < instruction.i.toInt()) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.interchange(instruction.i)
        stack.pop()
        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeDROP(state: VmStateRunning, instruction: DROP): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null ||   stack.depth < 1) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.pop()
        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeNIP(state: VmStateRunning, instruction: NIP): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null ||   stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.interchange(1)
        stack.pop()
        state.copy(stack = Maybe.of(stack))
    }
}
