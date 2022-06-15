package fr.dila.solonepg.elastic.models.search;

import fr.dila.st.core.helper.PaginationHelper;
import java.util.Collection;

public abstract class AbstractCriteria {
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    private Collection<String> permsUtilisateur;
    private boolean hasDroitsNomination;
    private Integer page;
    private Integer pageSize;
    private Collection<String> colonnes;
    private Integer total;

    protected AbstractCriteria() {
        this.page = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Collection<String> getPermsUtilisateur() {
        return permsUtilisateur;
    }

    public void setPermsUtilisateur(Collection<String> permsUtilisateur) {
        this.permsUtilisateur = permsUtilisateur;
    }

    public boolean isHasDroitsNomination() {
        return hasDroitsNomination;
    }

    public void setHasDroitsNomination(boolean hasDroitsNomination) {
        this.hasDroitsNomination = hasDroitsNomination;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Collection<String> getColonnes() {
        return colonnes;
    }

    public void setColonnes(Collection<String> colonnes) {
        this.colonnes = colonnes;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public int getFrom() {
        return (int) PaginationHelper.calculeFromES(page, pageSize, total);
    }
}
