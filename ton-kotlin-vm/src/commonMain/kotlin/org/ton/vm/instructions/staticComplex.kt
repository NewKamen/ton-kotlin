package org.ton.vm.instructions

import org.ton.asm.stackcomplex.PUXC
import org.ton.asm.stackcomplex.XCHG2
import org.ton.asm.stackcomplex.XCHG3
import org.ton.asm.stackcomplex.XCPU
import org.ton.block.Maybe
import org.ton.vm.VmState
import org.ton.vm.VmStateException
import org.ton.vm.VmStateRunning

internal fun executeXCHG3(state: VmStateRunning, instruction: XCHG3): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth > 1
        || stack.depth < instruction.i.toInt()
        || stack.depth < instruction.j.toInt()
        || stack.depth < instruction.k.toInt()
    ) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {

        stack.interchange(2u, instruction.i)
        stack.interchange(1u, instruction.j)
        stack.interchange(instruction.k)

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeXCHG2(state: VmStateRunning, instruction: XCHG2): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth > 1
        || stack.depth < instruction.i.toInt()
        || stack.depth < instruction.j.toInt()
    ) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {

        stack.interchange(1u, instruction.i)
        stack.interchange(instruction.j)

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executeXCPU(state: VmStateRunning, instruction: XCPU): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth > 1
        || stack.depth < instruction.i.toInt()
        || stack.depth < instruction.j.toInt()
    ) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {

        stack.interchange(instruction.i)
        stack.push(stack[instruction.j.toInt()])

        state.copy(stack = Maybe.of(stack))
    }
}

internal fun executePUXC(state: VmStateRunning, instruction: PUXC): VmState {

    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth > 1
        || stack.depth < instruction.i.toInt()
        || stack.depth < instruction.j.toInt()
    ) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        stack.push(stack[instruction.i.toInt()])

        val s0 = stack.pop()
        val s1 = stack.pop()

        stack.interchange(instruction.j)

        state.copy(stack = Maybe.of(stack))
    }
}
