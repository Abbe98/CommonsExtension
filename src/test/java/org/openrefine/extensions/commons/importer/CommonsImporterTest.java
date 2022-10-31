package org.openrefine.extensions.commons.importer;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.refine.ProjectMetadata;
import com.google.refine.RefineServlet;
import com.google.refine.importing.ImportingJob;
import com.google.refine.importing.ImportingManager;
import com.google.refine.model.Cell;
import com.google.refine.model.Project;
import com.google.refine.util.ParsingUtilities;

public class CommonsImporterTest {

    protected RefineServlet servlet;

    // dependencies
    private Project project;
    private ProjectMetadata metadata;
    private ImportingJob job;

    /**
     * Test column names upon project creation as well as reconciled cells
     */
    @Test
    public void testParse() throws Exception {

        servlet = new RefineServlet();
        ImportingManager.initialize(servlet);
        project = new Project();
        metadata = new ProjectMetadata();
        metadata.setName("Commons Import Test Project");
        job = Mockito.mock(ImportingJob.class);
        ObjectNode options = ParsingUtilities.evaluateJsonStringToObjectNode(
                "{\"categoryJsonValue\":[{\"category\":\"Category:Costa Rica\",\"depth\":\"0\"}],\"skipDataLines\":0,"
                + "\"limit\":-1,\"disableAutoPreview\":false,\"categoriesColumn\":true,\"mIdsColumn\":true}");
        List<Exception> exceptions = new ArrayList<Exception>();

        CommonsImporter.parse(project, metadata, job, 0, options, exceptions);
        project.update();
        Cell cell = project.rows.get(0).cells.get(0);

        Assert.assertEquals(project.columnModel.columns.get(0).getName(), "File");
        Assert.assertEquals(project.columnModel.columns.get(1).getName(), "M-ids");
        Assert.assertEquals(project.columnModel.columns.get(2).getName(), "Categories");
        Assert.assertEquals(cell.recon.match.id, "M114916202");
        Assert.assertEquals(cell.recon.match.name, "File:2006-08-08 Vista Parque Nacional Volcan Arenal, Costa Rica.jpg");

    }
}
