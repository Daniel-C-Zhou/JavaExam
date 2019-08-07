# Tasks

## Task 1: parse SDF file, and save parsed result to JSON file
- Input: an SDF file. See example input file `examples/sample_1.sdf` or `examples/sample_2.sdf`
- Output: a JSON file. See example output file `examples/output_example.json` (part of the `Structure` field is omitted)
- See `Ref. 1: SD Files` below for the format of an SDF file
- Note: the field names (e.g. `ID`, `Chemical_Name`, `Exact_Mass`) are not fixed. You should not write hard-coded field names in your code.

## Task 2: convert the MOL text to PNG image file
- Input: the MOL text extracted from Task 1 (value of `Structure` field). See example MOL file `examples/sample_1_01.mol`
- Output: a PNG file. See example image file `examples/sample_1_01.png`
- See `Ref. 2: CDWS API` below for how to generate the PNG
- Note: there may be multiple records in an SDF file (each record has one structure/MOL text). Multiple image files should be generated for one SDF file.



# Coding Guide
- You must use Java instead of other programming languages
- You should use git (local) to manage the code history
- You could setup the projects using your favorite IDE
- You could use any frameworks or libraries
- You could search proper resources from the Internet
- It's better to have reasonable packages, classes, methods and variables
- It's better to have necessary code comments
- It's better to have unit test
- The use of non-blocking IO would be a plus



# References

## Ref. 1: SD Files
The details about SD Files and MOL file can be found in `docs/ctfile.pdf` ("Chapter 6: SDfiles") or `docs/SDfile.PNG`.

## Ref. 2: CDWS API
CDWS (ChemDraw Web Service) is an HTTP service that can support multiple chemical calculation. You can call this service to generate image from MOL text.

- URL: https://chemdrawdirect.perkinelmer.cloud/rest/generateImage
- HTTP Method: POST
- Request body format (JSON):
```json
{
	"chemData": <MOL text here>,
	"chemDataType": "chemical/x-mdl-molfile",
	"imageType": "image/png"
}
```
- Response data format: Image data

For more details, please refer to https://chemdrawdirect.perkinelmer.cloud/rest/help/operations/ToImageResource.
