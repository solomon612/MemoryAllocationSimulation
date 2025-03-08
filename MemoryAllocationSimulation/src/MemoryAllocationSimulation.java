import java.util.*;

class MemoryBlock {
    int start, size;
    boolean allocated;

    public MemoryBlock(int start, int size) {
        this.start = start;
        this.size = size;
        this.allocated = false;
    }
}

class MemoryAllocator {
    private List<MemoryBlock> memory;

    public MemoryAllocator(int totalSize) {
        memory = new ArrayList<>();
        memory.add(new MemoryBlock(0, totalSize));
    }

    public boolean allocateFirstFit(int size) {
        for (MemoryBlock block : memory) {
            if (!block.allocated && block.size >= size) {
                splitBlock(block, size);
                return true;
            }
        }
        return false;
    }

    public boolean allocateBestFit(int size) {
        MemoryBlock bestFit = null;
        for (MemoryBlock block : memory) {
            if (!block.allocated && block.size >= size) {
                if (bestFit == null || block.size < bestFit.size) {
                    bestFit = block;
                }
            }
        }
        if (bestFit != null) {
            splitBlock(bestFit, size);
            return true;
        }
        return false;
    }

    private void splitBlock(MemoryBlock block, int size) {
        if (block.size > size) {
            memory.add(memory.indexOf(block) + 1, new MemoryBlock(block.start + size, block.size - size));
        }
        block.size = size;
        block.allocated = true;
    }

    public boolean deallocate(int start) {
        for (MemoryBlock block : memory) {
            if (block.start == start && block.allocated) {
                block.allocated = false;
                mergeFreeBlocks();
                return true;
            }
        }
        return false;
    }

    private void mergeFreeBlocks() {
        List<MemoryBlock> newMemory = new ArrayList<>();
        for (MemoryBlock block : memory) {
            if (!newMemory.isEmpty() && !block.allocated && !newMemory.get(newMemory.size() - 1).allocated) {
                newMemory.get(newMemory.size() - 1).size += block.size;
            } else {
                newMemory.add(block);
            }
        }
        memory = newMemory;
    }

    public void printMemory() {
        for (MemoryBlock block : memory) {
            System.out.println("Start: " + block.start + " Size: " + block.size + " Allocated: " + block.allocated);
        }
    }
}

public class MemoryAllocationSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MemoryAllocator allocator = new MemoryAllocator(100);

        while (true) {
            System.out.println("1. Allocate First-Fit\n2. Allocate Best-Fit\n3. Deallocate\n4. Show Memory\n5. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter block size: ");
                    int ffSize = scanner.nextInt();
                    System.out.println(allocator.allocateFirstFit(ffSize) ? "Allocated successfully" : "Allocation failed");
                    break;
                case 2:
                    System.out.print("Enter block size: ");
                    int bfSize = scanner.nextInt();
                    System.out.println(allocator.allocateBestFit(bfSize) ? "Allocated successfully" : "Allocation failed");
                    break;
                case 3:
                    System.out.print("Enter start address: ");
                    int start = scanner.nextInt();
                    System.out.println(allocator.deallocate(start) ? "Deallocated successfully" : "Deallocation failed");
                    break;
                case 4:
                    allocator.printMemory();
                    break;
                case 5:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}

