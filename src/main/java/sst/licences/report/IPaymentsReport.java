package sst.licences.report;

import sst.licences.model.Payment;

import java.io.IOException;
import java.util.List;

public interface IPaymentsReport {
    IPaymentsReport input(List<Payment> input);

    IPaymentsReport format() throws IOException;

    String output();
}
