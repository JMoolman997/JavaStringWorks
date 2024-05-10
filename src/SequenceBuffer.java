package src;

import java.util.LinkedList;
import java.util.ListIterator;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

/**
 * Interface defining the structure and capabilities of a sequence buffer,
 * which manages a series of subsequences,
 * allowing for their addition, retrieval,
 * and processing status checks.
 */
public interface SequenceBuffer {

    /**
     * Represents a single subsequence within a sequence buffer,
     * including its start and end indices,
     * the associated result string, and a processing status flag.
     */
    class SubSequence {

        public Integer iD; // Identifier for the subsequence within the buffer

        private boolean processed; // Indicates if this subsequence has been processed
        private int startIndex; // Starting index of the subsequence within the main sequence
        private int endIndex; // Ending index of the subsequence within the main sequence
        private String result; // Result associated with this subsequence

        /**
         * Constructs a new SubSequence with specified start and end indices and result string.
         * The subsequence is marked as processed if the result string is not empty.
         *
         * @param startIndex the starting index of the subsequence
         * @param endIndex the ending index of the subsequence
         * @param result the result associated with the subsequence
         */
        public SubSequence(int startIndex, int endIndex, String result) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.result = result;
            this.processed = !result.isEmpty();
        }

        /**
         * Checks if the subsequence has been processed.
         *
         * @return true if the subsequence is processed, false otherwise
         */
        public boolean isProcessed() {
            return this.processed;
        }

        /**
         * Retrieves the start index of the subsequence.
         *
         * @return the start index
         */
        public int getStartIndex() {
            return this.startIndex;
        }

        /**
         * Sets the start index of the subsequence.
         *
         * @param startIndex the new start index
         */
        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        /**
         * Retrieves the end index of the subsequence.
         *
         * @return the end index
         */
        public int getEndIndex() {
            return this.endIndex;
        }

        /**
         * Sets the end index of the subsequence.
         *
         * @param endIndex the new end index
         */
        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

        /**
         * Retrieves the result associated with the subsequence.
         *
         * @return the result string
         */
        public String getResult() {
            return this.result;
        }

        /**
         * Sets the result string of the subsequence and updates its processed status.
         * A non-empty result string marks the subsequence as processed.
         *
         * @param result the new result string
         */
        public void setResult(String result) {
            this.result = result;
            this.processed = !result.isEmpty();
        }

        /**
         * Calculates and returns the length of the subsequence based on its indices.
         *
         * @return the length of the subsequence
         */
        public int getLength() {
            return this.endIndex - this.startIndex;
        }
    }
    
    /**
     * Adds a subsequence to the sequence buffer.
     *
     * @param startIndex the starting index of the subsequence within the main sequence
     * @param endIndex the ending index of the subsequence within the main sequence
     * @param result the result or value associated with this subsequence
     */
    void addSubSequence(int startIndex, int endIndex, String result);

    /**
     * Adds a sub-sequence to the current sequence buffer.
     *
     * @param subSequence The sub-sequence to be added. It must not be null.
     */    
    void addSubSequence(SubSequence subSequence);
    
    /**
     * Retrieves a subsequence by its index in the sequence buffer.
     *
     * @param index the index of the subsequence to retrieve
     * @return the SubSequence at the specified index
     */
    SubSequence getSubSequenceByIndex(int index);

    /**
     * Retrieves the index of the given sub-sequence within the current sequence buffer.
     *
     * @param subSequence The sub-sequence to find. It must not be null.
     * @return The index of the sub-sequence if it is found, or -1 if it is not present.
     */    
    int getSubSequenceIndex(SubSequence subSequence);

    /**
     * Retrieves the first subsequence in the sequence buffer that has not been processed.
     *
     * @return the first unprocessed SubSequence, or null if all are processed
     */
    SubSequence getFirstUnprocessedSubSequence();

    /**
     * Checks if all subsequences in the buffer have been processed.
     *
     * @return true if all subsequences are processed, false otherwise
     */
    boolean isProcessed();

    /**
     * Retrieves the original sequence of data as an array of strings.
     *
     * @return an array of strings representing the original sequence
     */
    String[] getOriginalSequence();

    /**
     * Converts the results of all subsequences into a single string representation.
     *
     * @return a string representing the results of all subsequences concatenated together
     */
    String resultToString();

    /**
     * Gets the size of the subsequence list.
     *
     * @return the number of subsequences in the buffer
     */
    int getSubsequenceSize();

    /**
     * Prints the details of all subsequences to the standard output.
     */
    void printSubSequences();

    /**
     * Prints the entire sequence buffer including original sequence
     * and subsequences to the standard output.
     */
    void printSequenceBuffer();
}

/**
 * Abstract implementation of the SequenceBuffer, providing common functionality
 * that can be used by all concrete buffer implementations. This class initializes
 * the sequence storage and implements basic operations defined in the SequenceBuffer interface.
 * 
 * @param <T> the type of elements held in the buffer 
 */
abstract class AbstractSequenceBuffer<T> implements SequenceBuffer {

    protected ArrayList<T> originalSequence; // Holds the original sequence of data.
    protected LinkedList<SubSequence> subSequences; // List of subsequences within the buffer.
    protected LinkedList<Integer> subSequenceID; // List of identifiers for each subsequence.

    /**
     * Checks if all subsequences within the buffer have been processed.
     *
     * @return true if all subsequences are processed, otherwise false
     */
    public boolean isProcessed() {
        for (SubSequence subSeq : subSequences) {
            if (!subSeq.isProcessed()) {
                // Found an unprocessed SubSequence, return false
                return false;
            }
        }
        // All SubSequences are processed
        return true;
    }

    /**
     * Retrieves the number of subsequences currently stored in the buffer.
     *
     * @return the number of subsequences in the buffer
     */
    public int getSubsequenceSize() {
        return this.subSequences.size();
    }

    /**
     * Retrieves a SubSequence by its index in the list.
     * @param index The index of the SubSequence to retrieve.
     * @return The SubSequence at the specified index.
     */
    public SubSequence getSubSequenceByIndex(int index) {
        if (index < 0 || index >= subSequences.size()) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d is out of bounds [0, %d]",
                index, subSequences.size() - 1)
            );
        }
        return subSequences.get(index); // Directly access the LinkedList
    }
 
    /**
     * Returns the index of the specified sub-sequence within the list of sub-sequences.
     *
     * @param subSequence The sub-sequence to find. It must not be null.
     * @return The index of the sub-sequence if it is found in the list; otherwise, -1.
     */    
    public int getSubSequenceIndex(SubSequence subSequence) {
        return subSequences.indexOf(subSequence);
    }

    /**
     * Retrieves the first subsequence from the buffer that has not been processed yet.
     * This method iterates through the list of subsequences and returns the first one
     * that is still unprocessed.
     *
     * @return the first unprocessed SubSequence if any; otherwise,
     *         null if all subsequences
     *         are processed or the list is empty
     */
    public SubSequence getFirstUnprocessedSubSequence() {
        for (SubSequence sub : subSequences) {
            if (!sub.isProcessed()) {
                // Found an unprocessed SubSequence, return it
                return sub;
            }
        }
        // Return null if all subsequences are processed or if there are no subsequences
        return null;
    }

    /**
     * Returns the original sequence as an array of strings. This method processes the
     * original sequence stored as a list of objects, converting each object into a string.
     * The method ensures that only characters or strings are included in the original sequence,
     * throwing an exception for other types.
     *
     * @return an array of strings representing the original sequence
     */
    public String[] getOriginalSequence() {
        if (this.originalSequence.isEmpty()) {
            // Return an empty String array if the original sequence is empty
            return new String[0];
        }

        String[] stringArray = new String[this.originalSequence.size()];
        for (int i = 0; i < this.originalSequence.size(); i++) {
            Object element = this.originalSequence.get(i);
            // Check if the element is of type Character or String and convert accordingly
            if (element instanceof Character) {
                stringArray[i] = String.valueOf(element); // Convert Character to String
            } else if (element instanceof String) {
                stringArray[i] = (String) element; // Cast directly to String
            } else {
                // If the element is neither a Character nor a String, throw an exception
                throw new IllegalStateException(
                    "Unsupported type in originalSequence. Expected Char or Str not found: " 
                    + element.getClass().getSimpleName()
                );
            }
        }

        return stringArray;
    }

    /**
     * Adds the specified sub-sequence to the current sequence buffer
     * by extracting its start index,
     * end index, and result value.
     *
     * @param subSequence The sub-sequence to be added. It must not be null.
     */    
    public void addSubSequence(SubSequence subSequence) {
        if (subSequence == null) {
            throw new IllegalArgumentException(
                "The sub-sequence must not be null."
            );
        }
        int startIndex = subSequence.getStartIndex();
        int endIndex = subSequence.getEndIndex();
        String result = subSequence.getResult();
        this.addSubSequence(startIndex, endIndex, result);
    }

    /**
     * Adds a new subsequence to the sequence buffer, only if certain conditions are met.
     * The method first validates the indices and the result string, throwing an exception
     * for invalid arguments. It then checks if the new subsequence is unprocessed and
     * if there are no existing subsequences in the buffer. If these conditions are met,
     * the subsequence is added; otherwise, the subsequence is processed further.
     *
     * @param startIndex the starting index of the new subsequence
     * @param endIndex the ending index of the new subsequence
     * @param result the result or data associated with this subsequence
     */
    public void addSubSequence(int startIndex, int endIndex, String result) {
        // Validate indices and result string
        if (startIndex < 0 || endIndex < startIndex) {
            throw new IllegalArgumentException("Invalid start or end index");
        }
        if (result == null) {
            throw new IllegalArgumentException("Result string cannot be null");
        }

        SubSequence subSequence = new SubSequence(startIndex, endIndex, result);
        // Ensure this code is only executed if processed is false
        // and the size of subSequences is 0
        if (!subSequence.isProcessed() && this.subSequences.isEmpty()) {
            this.subSequences.add(subSequence);
            assignsubSequenceID(subSequence);
        } else {
            processNewSubSequence(subSequence);
        }
    }

    /**
     * Processes a new subsequence against existing subsequences in the buffer.
     * This method uses a list iterator to traverse existing subsequences and determines
     * the relationship between them and the new subsequence using a switch case based on
     * specific matching criteria (e.g., exact match, start match). Depending on the match type,
     * it may update existing subsequences or modify them accordingly.
     *
     * @param newSub the new subsequence to be processed
     */
    private void processNewSubSequence(SubSequence newSub) {
        ListIterator<SubSequence> iterator = this.subSequences.listIterator();
        while (iterator.hasNext()) {
            SubSequence existingSub = iterator.next();
            String caseType = determineCase(newSub, existingSub);

            switch (caseType) {
                case "exactMatch":
                    updateExistingSubSequence(newSub, existingSub);
                    return;
                case "startMatch":
                    splitAndProcessSubsequence(
                        iterator,
                        existingSub,
                        newSub,
                        true
                    );
                    return;
                case "endMatch":
                    splitAndProcessSubsequence(
                        iterator, 
                        existingSub, 
                        newSub, 
                        false
                    );
                    return;
                case "fullEncapsulation":
                    splitAndEncapsulateSubsequence(
                        iterator, 
                        existingSub,
                        newSub
                    );
                    return;
                case "noMatch":
                    // add error message
                    break;
                default:
                    throw new IllegalStateException(
                        "Unexpected case: " + caseType
                    );
            }
        }
    }

    /**
     * Determines the relationship between a new subsequence and an existing 
     * unprocessed subsequence.
     * Compares their start and end indices to classify the 
     * relationship: exact match, start match, 
     * end match, full encapsulation, or no match.
     * 
     * Definitions:
     * - "exactMatch": Identical start and end indices for both subsequences.
     * - "startMatch": New subsequence starts at the same index but ends earlier.
     * - "endMatch": New subsequence ends at the same index but starts later.
     * - "fullEncapsulation": New subsequence starts after and ends before the existing one.
     * - "noMatch": None of the above conditions are met.
     *
     * This method is only called if the existing subsequence has not been processed.
     *
     * @param newSub The new subsequence being compared.
     * @param existingSub The existing subsequence to compare against.
     * @return A string representing the type of match: "exactMatch", "startMatch", "endMatch", 
     *         "fullEncapsulation", or "noMatch".
     */
    private String determineCase(SubSequence newSub, SubSequence existingSub) {
        if (!existingSub.isProcessed()) {
            if (newSub.getStartIndex() == existingSub.getStartIndex() 
                && newSub.getEndIndex() == existingSub.getEndIndex()) {
                return "exactMatch";
            } else if (newSub.getStartIndex() == existingSub.getStartIndex() 
                && newSub.getEndIndex() < existingSub.getEndIndex()) {
                return "startMatch";
            } else if (newSub.getEndIndex() == existingSub.getEndIndex() 
                && newSub.getStartIndex() > existingSub.getStartIndex()) {
                return "endMatch";
            } else if (newSub.getStartIndex() > existingSub.getStartIndex() 
                && newSub.getEndIndex() < existingSub.getEndIndex()) {
                return "fullEncapsulation";
            }
        }
        return "noMatch";
    }

    /**
     * Updates an existing subsequence with the result from a new subsequence. This method
     * simply replaces the result of the existing subsequence with that of the new subsequence.
     *
     * @param newSub the new subsequence whose result is to be used
     * @param existingSub the existing subsequence that will be updated
     */
    private void updateExistingSubSequence(SubSequence newSub, SubSequence existingSub) {
        existingSub.setResult(newSub.getResult());
    }

    /**
     * Modifies an existing subsequence based on overlap with a 
     * new subsequence and splits if needed.
     * If the start of the new subsequence aligns with the existing one, 
     * it adjusts the end of the existing subsequence and may add 
     * a new unprocessed subsequence for the remaining part.
     * Conversely, if the end aligns, it adjusts the start of the existing subsequence
     * and adds a new unprocessed subsequence for the leading part.
     *
     * @param iterator The ListIterator for the list of subsequences, 
     *        allowing modification during iteration.
     * @param existingSub The existing subsequence that 
     *        overlaps with the new subsequence.
     * @param newSub The new subsequence that overlaps 
     *        with the existing subsequence.
     * @param isStartAligned A boolean indicating if the start 
     *        of the new and existing subsequences align.
     */
    private void splitAndProcessSubsequence(
        ListIterator<SubSequence> iterator,
        SubSequence existingSub,
        SubSequence newSub,
        boolean isStartAligned
        ) {

        if (isStartAligned) {
            // Handle the case where the new subsequence starts where existingSub starts
            SubSequence newUnprocessedSub = new SubSequence(
                newSub.getEndIndex() + 1,
                existingSub.getEndIndex(),
                ""
            );
            existingSub.setEndIndex(newSub.getEndIndex());
            existingSub.setResult(newSub.getResult());
            
            if (newUnprocessedSub.getStartIndex() <= newUnprocessedSub.getEndIndex()) {
                iterator.add(newUnprocessedSub);
            }
        } else {
            // Handle the case where the new subsequence ends where existingSub ends
            SubSequence newUnprocessedSub = new SubSequence(
                existingSub.getStartIndex(),
                newSub.getStartIndex() - 1,
                ""
            );
            existingSub.setStartIndex(newSub.getStartIndex());
            existingSub.setResult(newSub.getResult());
            
            if (newUnprocessedSub.getStartIndex() <= newUnprocessedSub.getEndIndex()) {
                iterator.add(newUnprocessedSub);
            }
        }
        
        // Update IDs after modifying subsequences
        reassignsubSequenceIDs();
    }

    /**
     * Splits an existing subsequence around a new subsequence fully encapsulated 
     * within the existing one.This method adjusts the existing subsequence
     * to match the new one and adds new subsequences for 
     * any leading and trailing parts that remain unprocessed.
     *
     * @param iterator The ListIterator for the list of subsequences, 
     *                 allowing modification during iteration.
     * @param existingSub The existing subsequence that is being encapsulated 
     *                    by the new subsequence.
     * @param newSub The new subsequence encapsulated within the existing subsequence.
     */
    private void splitAndEncapsulateSubsequence(
        ListIterator<SubSequence> iterator, 
        SubSequence existingSub, 
        SubSequence newSub
        ) {

        SubSequence before = new SubSequence(
            existingSub.getStartIndex(),
            newSub.getStartIndex() - 1,
            ""
        );
        SubSequence after = new SubSequence(
            newSub.getEndIndex() + 1,
            existingSub.getEndIndex(),
            ""
        );

        existingSub.setStartIndex(newSub.getStartIndex());
        existingSub.setEndIndex(newSub.getEndIndex());
        existingSub.setResult(newSub.getResult());

        if (before.getEndIndex() >= before.getStartIndex()) {
            iterator.add(before);
        }
        if (after.getEndIndex() >= after.getStartIndex()) {
            iterator.add(after);
        }
        reassignsubSequenceIDs();
    }

    /**
     * Assigns a unique identifier to a new subsequence based on its start 
     * index relative to existing subsequences. This method iterates 
     * through the ordered list of subsequences, comparing start 
     * indices to determine the correct position for the new subsequence. 
     * It assigns an iD reflecting this position, ensuring that IDs are 
     * sequentially ordered by start index.
     * 
     * If the new subsequence should be placed after all existing ones, 
     * it receives the next available iD.After assigning an iD, 
     * this method also calls {@code reassignsubSequenceIDs} to ensure all 
     * subsequences maintain consecutive iD ordering.
     *
     * @param newSubSequence The new subsequence that needs an iD assigned.
     */
    private void assignsubSequenceID(SubSequence newSubSequence) {
        int subSeqRank = 1;  // Initialize rank starting from 1
        boolean isPlaced = false;

        // Loop through existing subsequences to find the correct position for the new subsequence
        for (SubSequence subSequence : this.subSequences) {
            if (newSubSequence.getStartIndex() < subSequence.getStartIndex()) {
                // If the new subsequence starts before the current one
                // and assign the current rank and stop
                newSubSequence.iD = subSeqRank;
                isPlaced = true;
                break;
            }
            subSeqRank++;  // Otherwise, move to the next rank
        }

        // If the new subsequence starts after all existing ones,
        // takes the next available rank
        if (!isPlaced) {
            newSubSequence.iD = subSeqRank;
        }

        // reassign IDs to all subsequences to maintain consecutive ordering 
        reassignsubSequenceIDs();
    }

    /**
     * Reassigns sequential identifiers to all subsequences in the buffer. 
     * This method first sorts the subsequences by their start and end indices
     *  to ensure that IDs are assigned in a meaningful order.
     * Each subsequence receives an iD corresponding to its position in the sorted list, 
     * starting from 1 and incrementing sequentially.
     */
    private void reassignsubSequenceIDs() {
        sortSubSequences();
        int rank = 1;
        for (SubSequence subSequence : this.subSequences) {
            subSequence.iD = rank++;  // Increment rank for each subsequence
        }
    }

    /**
     * Sorts the subsequences within the buffer based on their start index,
     * and in the case of ties, by their end index.
     * This method utilizes a custom comparator within a {@code Collections.sort} call,
     * ensuring subsequences are ordered first by the starting index,
     * and if two subsequences start at the same point, then by the ending index.
     */
    private void sortSubSequences() {
        Collections.sort(subSequences, new Comparator<SubSequence>() {
            @Override
            public int compare(SubSequence o1, SubSequence o2) {
                if (o1.getStartIndex() != o2.getStartIndex()) {
                    return Integer.compare(o1.getStartIndex(), o2.getStartIndex());
                } else {
                    return Integer.compare(o1.getEndIndex(), o2.getEndIndex());
                }
            }
        });
    }
   
}

/**
 * A concrete implementation of {@link AbstractSequenceBuffer} specifically for 
 * handling character sequences.
 * This class extends the abstract functionality to manage a sequence of characters, 
 * allowing for operations like adding subsequences and extracting substrings 
 * based on those subsequences.
 */
class CharBuffer extends AbstractSequenceBuffer<Character> {
    
    /**
     * Constructs a CharBuffer from an array of characters, initializing the original sequence
     * and setting up an initial subsequence that spans the entire array.
     *
     * @param inputArray the array of characters to be managed by this buffer
     */
    CharBuffer(char[] inputArray) {
        this.originalSequence = new ArrayList<Character>();
        for (char c : inputArray) {
            this.originalSequence.add(c); // Autoboxing converts char to Character
        }
        this.subSequences = new LinkedList<>();
        this.addSubSequence(0, inputArray.length - 1, "");
    }

    /**
     * Returns a string representation of the results of the subsequences in the buffer,
     * concatenating them with a hyphen "-" as a delimiter.
     * Unprocessed subsequences are represented by "*".
     *
     * @return a formatted string representation of the subsequences' results
     */
    public String resultToString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;  // Flag to track the first append operation

        for (SubSequence subSequence : subSequences) {
            // Append delimiter if not the first subsequence
            if (!isFirst) {
                stringBuilder.append("-");  
            }
            // Append result or "*" based on processed status
            if (subSequence.isProcessed()) {
                stringBuilder.append(subSequence.getResult());
            } else {
                stringBuilder.append("*");
            }
              
            isFirst = false;  // Update flag after first append
        }

        return stringBuilder.toString();
    }

    /**
     * Extracts the substring from the original sequence that 
     * corresponds to the indices of a given subsequence.
     *
     * @param subSeq the subsequence from which to extract the substring
     * @return the substring from the original sequence corresponding to the 
     *         subsequence's indices
     */
    public String extractSubstringFromSubSequence(SubSequence subSeq) {
        if (subSeq == null) {
            throw new IllegalArgumentException("SubSequence cannot be null.");
        }
    
        if (subSeq.getStartIndex() < 0 || subSeq.getEndIndex() >= originalSequence.size()) {
            throw new IndexOutOfBoundsException("SubSequence indices are out of bounds.");
        }
    
        if (subSeq.getStartIndex() > subSeq.getEndIndex()) {
            throw new IllegalArgumentException("Start index cannot be greater than end index.");
        }
    
        StringBuilder builder = new StringBuilder();
        for (int i = subSeq.getStartIndex(); i <= subSeq.getEndIndex(); i++) {
            Object element = originalSequence.get(i);
            builder.append(element.toString()); 
        }
    
        return builder.toString();
    }

    /**
     * Prints the details of all subsequences in the buffer,
     * including IDs, indices, substrings, and processing status.
     */
    public void printSubSequences() {
        System.out.println("_Subsequences_");
        for (SubSequence subSeq : subSequences) {
            String output = String.format("iD: %s,Indx: [%d:%d],Str: '%s',Proc: %s,Result: '%s'",
                                          subSeq.iD, subSeq.getStartIndex(), subSeq.getEndIndex(),
                                          extractSubstringFromSubSequence(subSeq),
                                          subSeq.isProcessed(), subSeq.getResult());
            System.out.println(output);
        }
    }

    /**
     * Prints comprehensive details about the sequence buffer,
     * including the size and contents of the original sequence
     * and detailed information about each subsequence.
     */
    public void printSequenceBuffer() {
        System.out.println("__Sequence Buffer Details__");
        System.out.println("Size of Original Sequence: " + originalSequence.size());
        System.out.println("Contents of Original Sequence:");
        for (int i = 0; i < originalSequence.size(); i++) {
            char item = originalSequence.get(i);
            System.out.print("[" + i + ":" + item + "] ");
        }
        System.out.println("\nNumber of SubSequences: " + subSequences.size());
        printSubSequences();  // This will print the details of each subsequence
    }
}

/**
 * A concrete implementation of {@link AbstractSequenceBuffer} tailored 
 * for managing buffers of strings.
 * This class is specifically designed to handle operations on a sequence of strings, 
 * including the addition of subsequences and manipulation of these subsequences 
 * based on defined criteria.
 */
class BrfBuffer extends AbstractSequenceBuffer<String> {

    /**
     * Constructs a BrfBuffer from an array of strings.
     * Initializes the original sequence with the provided
     * array and sets up an initial subsequence covering the entire array.
     *
     * @param inputArray the array of strings to be managed by this buffer
     */
    BrfBuffer(String[] inputArray) {
        this.originalSequence = new ArrayList<String>();
        this.subSequences = new LinkedList<>();
        this.subSequenceID = new LinkedList<>();

        try {
            for (String brf : inputArray) {
                this.originalSequence.add(brf);
            }
            this.addSubSequence(0, inputArray.length - 1, "");
        } catch (Exception e) {
            System.err.println("Initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns a string representation of the results of subsequences in the buffer.
     * Concatenates results of processed subsequences, and uses "*" for unprocessed ones.
     *
     * @return a string representing the concatenated results of subsequences
     */
    public String resultToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SubSequence subSequence : subSequences) {
            if (subSequence.isProcessed()) {
                stringBuilder.append(subSequence.getResult());
            } else {
                stringBuilder.append("*");
            }
            
        }
        return stringBuilder.toString();
    }

    /**
     * Extracts a substring from the original sequence based on the indices of a given subsequence.
     * Validates the subsequence's indices to ensure they are within the 
     * bounds of the original sequence.
     *
     * @param subSeq the subsequence from which to extract the substring
     * @return the extracted substring
     */
    public String extractSubstringFromSubSequence(SubSequence subSeq) {
        if (subSeq == null) {
            throw new IllegalArgumentException("SubSequence cannot be null.");
        }

        if (subSeq.getStartIndex() < 0 
            || subSeq.getEndIndex() >= this.originalSequence.size()) {
            throw new IndexOutOfBoundsException("SubSequence indices are out of bounds.");
        }

        if (subSeq.getStartIndex() > subSeq.getEndIndex()) {
            throw new IllegalArgumentException("Start index cannot be greater than end index.");
        }

        StringBuilder builder = new StringBuilder();
        for (int i = subSeq.getStartIndex(); i <= subSeq.getEndIndex(); i++) {
            String element = originalSequence.get(i);
            if (i != subSeq.getStartIndex()) { 
                builder.append("-"); 
            }
            builder.append(element);
        }

        return builder.toString();
    }

    /**
     * Prints the details of all subsequences in the buffer, including IDs, indices, 
     * substrings,processing status, and results.
     */
    public void printSubSequences() {
        System.out.println("_Subsequences_");
        for (SubSequence subSeq : subSequences) {
            String output = String.format(
                "iD: %s, Indices: [%d:%d], Substring: '%s', Processed: %s, Result: '%s'",
                subSeq.iD, subSeq.getStartIndex(), subSeq.getEndIndex(),
                extractSubstringFromSubSequence(subSeq),
                subSeq.isProcessed(), subSeq.getResult()
            );
            System.out.println(output);
        }
    }

    /**
     * Prints comprehensive details about the sequence buffer, including its size and contents,
     * as well as details about each subsequence.
     */
    public void printSequenceBuffer() {

        System.out.println();  // newline
        System.out.println("+-------------------------------------------------------------+");
        System.out.println("| Sequence Buffer Details                                     |");
        System.out.println("+-------------------------------------------------------------+");
        System.out.println("Size of Original Sequence: " + originalSequence.size());
        System.out.println("+-------------------------------------------------------------+");
        System.out.println("| Contents of Original Sequence:                              |");
        System.out.println("+-------------------------------------------------------------+");
        System.out.println("| Index | Element | Original Sequence                          |");
        System.out.println("+-------------------------------------------------------------+");
        for (int i = 0; i < originalSequence.size(); i++) {
            String item = originalSequence.get(i);
            System.out.printf("| %-7d | %-7s | %-24s |%n", i, "", item);
        }
        System.out.println("+-------------------------------------------------------------+");
        System.out.println(String.format("| Number of SubSequences: ", +subSequences.size()));
        System.out.println("+-------------------------------------------------------------+");
        printSubSequences();
    }
}





