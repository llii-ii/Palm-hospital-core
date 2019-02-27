package org.mybatis.generator.api.dom.xml;

public class Attribute implements Comparable<Attribute>
{
    private String name;
    private String value;
    
    public Attribute(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public String getFormattedContent() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append("=\"");
        sb.append(this.value);
        sb.append('\"');
        return sb.toString();
    }
    
    @Override
    public int compareTo(final Attribute o) {
        if (this.name == null) {
            return (o.name == null) ? 0 : -1;
        }
        if (o.name == null) {
            return 0;
        }
        return this.name.compareTo(o.name);
    }
}
