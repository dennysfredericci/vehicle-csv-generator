package com.devoctans;

import net.datafaker.transformations.Field;
import net.datafaker.transformations.Schema;
import net.datafaker.transformations.SimpleField;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.IntConsumer;

public class CsvFileTransformer<IN> {
    public static final String DEFAULT_SEPARATOR = ";";
    public static final char DEFAULT_QUOTE = '"';
    
    private final String fileName;
    private final String separator;
    private final char quote;
    private final boolean withHeader;
    
    private CsvFileTransformer(String fileName, String separator, char quote, boolean withHeader) {
        this.fileName = fileName;
        this.separator = separator;
        this.quote = quote;
        this.withHeader = withHeader;
    }
    
    public static <IN> CsvFileTransformerBuilder<IN> builder() {
        return new CsvFileTransformerBuilder<>();
    }
    
    public CharSequence apply(IN input, Schema<IN, ?> schema) {
        Field<IN, ?>[] fields = schema.getFields();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            //noinspection unchecked
            SimpleField<Object, ?> f = (SimpleField<Object, ?>) fields[i];
            addLine(sb, f.transform(input));
            if (i < fields.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
    
    
    private void addLine(StringBuilder sb, Object transform) {
        if (transform instanceof CharSequence) {
            addCharSequence(sb, (CharSequence) transform);
        } else {
            sb.append(transform);
        }
    }
    
    private void addCharSequence(StringBuilder sb, CharSequence charSequence) {
        sb.append(quote);
        int i = 0;
        final int length = charSequence.length();
        for (int j = 0; j < length; j++) {
            final char c = charSequence.charAt(j);
            if (c == quote) {
                sb.append(charSequence, i, j + 1).append(quote);
                i = j + 1;
            }
        }
        sb.append(charSequence, i, length);
        sb.append(quote);
    }
    
    private void generateHeader(Schema<?, ?> schema, StringBuilder sb) {
        if (withHeader) {
            for (int i = 0; i < schema.getFields().length; i++) {
                addLine(sb, schema.getFields()[i].getName());
                if (i < schema.getFields().length - 1) {
                    sb.append(separator);
                }
            }
            sb.append(System.lineSeparator());
        }
    }
    
    public void generate(Schema<IN, ?> schema, int limit, IntConsumer progressReporter) throws IOException {
        
        try (FileWriter writer = new FileWriter(fileName)) {
            
            StringBuilder sbHeader = new StringBuilder();
            generateHeader(schema, sbHeader);
            writer.append(sbHeader);
            
            for (int i = 0; i < limit; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(apply(null, schema));
                
                if (i < limit - 1) {
                    sb.append(System.lineSeparator());
                }
                writer.append(sb);
                progressReporter.accept(i);
            }
            
            progressReporter.accept(limit);
            
            writer.flush();
        }
        
    }
    
    public static class CsvFileTransformerBuilder<IN> {
        private String separator = DEFAULT_SEPARATOR;
        private char quote = DEFAULT_QUOTE;
        private boolean withHeader = true;
        private String fileName;
        
        public CsvFileTransformerBuilder<IN> quote(char quote) {
            this.quote = quote;
            return this;
        }
        
        public CsvFileTransformerBuilder<IN> separator(String separator) {
            this.separator = separator;
            return this;
        }
        
        public CsvFileTransformerBuilder<IN> header(boolean header) {
            this.withHeader = header;
            return this;
        }
        
        public CsvFileTransformerBuilder<IN> fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public CsvFileTransformer<IN> build() {
            return new CsvFileTransformer<>(fileName, separator, quote, withHeader);
        }
        
        
    }
}

