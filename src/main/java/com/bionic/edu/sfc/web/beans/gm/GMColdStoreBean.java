package com.bionic.edu.sfc.web.beans.gm;

import com.bionic.edu.sfc.entity.FishParcel;
import com.bionic.edu.sfc.entity.FishShipSupply;
import com.bionic.edu.sfc.entity.FishShipSupplyStatus;
import com.bionic.edu.sfc.service.dao.IFishParcelService;
import com.bionic.edu.sfc.service.dao.IFishShipSupplyService;
import com.bionic.edu.sfc.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.CellEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.*;

/**
 * Created by docent on 09.11.14.
 */
@Named("gmCStore")
@Scope("request")
public class GMColdStoreBean {

    private static final Log LOG = LogFactory.getLog(GMColdStoreBean.class);

    @Autowired
    private IFishShipSupplyService fishShipSupplyService;

    @Autowired
    private IFishParcelService fishParcelService;

    private long fssId;

    private Long selectedFssId;

    private Set<FishParcel> readyForApproveParcels = new TreeSet<>(getReadyForApproveComparator());

    private Set<FishParcel> readyForSaleParcels = new TreeSet<>(getReadyForSaleComparator());

    private Set<FishShipSupply> deliveredToCMSupplies = new TreeSet<>(Util.getFishShipSupplyComparator());

    private List<FishParcel> filteredParcels;

    private FishShipSupply supply;

    @PostConstruct
    public void init() {
        LOG.info("init");
        deliveredToCMSupplies.clear();
        deliveredToCMSupplies.addAll(fishShipSupplyService.getAllTransportedToCM());

        updateWaitingParcels();
    }

    private void updateWaitingParcels() {
        LOG.info("selectedFssId: " + selectedFssId);
        if (selectedFssId != null) {
            fssId = selectedFssId;
        }

        if (deliveredToCMSupplies.size() > 0 && fssId <= 0) {
            LOG.info("Supplies: " + deliveredToCMSupplies);
            fssId = deliveredToCMSupplies.iterator().next().getId();
            selectedFssId = fssId;
        }

        LOG.info("FSSId: " + fssId);
        if (fssId != 0) {
            readyForApproveParcels.clear();
            supply = fishShipSupplyService.findById(fssId);
            List<FishParcel> fishParcels = fishParcelService.getAllForFishSupply(supply);
            readyForApproveParcels.addAll(fishParcels);
        }

        readyForSaleParcels.clear();
        readyForSaleParcels.addAll(fishParcelService.getAllUnsaled());
    }

    public void changeSupply() {
        updateWaitingParcels();
    }

    public void changeState(boolean approved) {
        updateWaitingParcels();
        if (approved) {
            supply.setStatus(FishShipSupplyStatus.READY_FOR_SALE);
        } else {
            supply.setStatus(FishShipSupplyStatus.REFUNDED);
        }
        fishShipSupplyService.update(supply);
        deliveredToCMSupplies.remove(supply);
        selectedFssId = null;
        fssId = -1;
        updateWaitingParcels();
    }

    public boolean haveWaitingForApprove() {
        return deliveredToCMSupplies.size() > 0;
    }

    public double getTotalWeight() {
        if (readyForApproveParcels.size() > 0) {
            return readyForApproveParcels.stream().mapToDouble(FishParcel::getWeight).sum();
        }
        return -1;
    }

    public Set<FishShipSupply> getDeliveredToCMSupplies() {
        return deliveredToCMSupplies;
    }

    public long getFssId() {
        return fssId;
    }

    public void setFssId(long fssId) {
        this.fssId = fssId;
    }

    public List<FishParcel> getReadyForApproveParcels() {
        List<FishParcel> result = new LinkedList<>();
        result.addAll(readyForApproveParcels);
        return result;
    }

    public void setReadyForApproveParcels(Set<FishParcel> readyForApproveParcels) {
        this.readyForApproveParcels = readyForApproveParcels;
    }

    public void setDeliveredToCMSupplies(Set<FishShipSupply> deliveredToCMSupplies) {
        this.deliveredToCMSupplies = deliveredToCMSupplies;
    }

    public Long getSelectedFssId() {
        return selectedFssId;
    }

    public void setSelectedFssId(Long selectedFssId) {
        this.selectedFssId = selectedFssId;
    }

    private Comparator<FishParcel> getReadyForSaleComparator() {
        return (fp1, fp2) -> {

            int result = fp1.getFishShipSupply().getSupplyCode().compareTo(fp2.getFishShipSupply().getSupplyCode());
            if (result != 0) {
                return result;
            }

            result = fp1.getFish().getName().compareTo(fp2.getFish().getName());
            if (result!= 0) {
                return result;
            }

            return fp1.getManufacturer().getName().compareTo(fp2.getManufacturer().getName());
        };
    }

    private Comparator<FishParcel> getReadyForApproveComparator() {
        return (fp1, fp2) -> {
            int result = fp1.getManufacturer().getName().compareTo(fp2.getManufacturer().getName());
            if (result != 0) {
                return result;
            }
            return fp1.getFish().getName().compareTo(fp2.getFish().getName());
        };
    }

    public void onCellEdit(CellEditEvent event) {
        Object newValue = event.getNewValue();
        Object oldValue = event.getOldValue();
        FishParcel fishParcel = getReadyForApproveParcels().get(event.getRowIndex());
        if(newValue != null && !newValue.equals(oldValue)) {
            LOG.info("Changed parcel: " + fishParcel);
        }
    }

    public List<FishParcel> getReadyForSaleParcels() {
        return new LinkedList(readyForSaleParcels);
    }

    public void setReadyForSaleParcels(Set<FishParcel> readyForSaleParcels) {
        this.readyForSaleParcels = readyForSaleParcels;
    }

    public List<FishParcel> getFilteredParcels() {
        return filteredParcels;
    }

    public void setFilteredParcels(List<FishParcel> filteredParcels) {
        this.filteredParcels = filteredParcels;
    }
}
